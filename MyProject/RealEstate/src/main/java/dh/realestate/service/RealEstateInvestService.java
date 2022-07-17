package dh.realestate.service;

import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.model.dto.RealEstateList;

import dh.realestate.model.entity.*;
import dh.realestate.model.entity.idgenerator.IdGenerator;
import dh.realestate.repository.*;
import dh.realestate.service.kakaomap.KakaoMapClient;
import dh.realestate.service.kakaomap.dto.KakaoMapCategoryRes;
import dh.realestate.service.kakaomap.dto.KakaoMapCoordinateRes;
import dh.realestate.service.molit.MolitClient;
import dh.realestate.service.molit.dto.MolitRealEstateRes;
import dh.realestate.service.molit.dto.xmlresponse.body.item.Item;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealEstateInvestService {

    @Value("${Date.Year}")
    private String currentYear;
    @Value("${Date.Month}")
    private String currentMonth;

    private final MolitClient molitClient;
    private final KakaoMapClient kakaoMapClient;

    private final RealEstateRepository realEstateRepository;
    private final SubwayRepository subwayRepository;
    private final SupermarketRepository supermarketRepository;
    private final RealEstateAndSubwayRepository realEstateAndSubwayRepository;
    private final RealEstateAndSupermarketRepository realEstateAndSupermarketRepository;

    public RealEstateList search(
            String region, String type, Integer lowPrice, Integer highPrice, Integer lowYear, Integer highYear)
            throws FileNotFoundException, UnsupportedEncodingException {

        // 예외 처리
        if ( (lowPrice == null || highPrice == null || !(lowPrice < highPrice)) ||
                (lowYear == null || highYear == null || !(lowYear < highYear)) ) {
            throw new RuntimeException("Query Parameter Error!");
        }

        // input parameter: 1.region, 2.type
        var molitRealEstateRes=
                molitClient.searchRealEstate(region, type);

        // query parameter 기준에 의한 filtering 진행. lowPrice <= price <= highPrice, lowYear <= year <= highYear
        List<Item> filteredRealEstate = filterByQueryParam(
                molitRealEstateRes, lowPrice, highPrice, lowYear, highYear
        );

        var realEstateList = new RealEstateList(
                region, type, currentYear + " " + currentMonth,
                new StringBuilder().append(lowPrice).append("~").append(highPrice).toString(),
                new StringBuilder().append(lowYear).append("~").append(highYear).toString(),
                filteredRealEstate.size()
        );

        // filtering 된 매물들 기준으로 KakaoMap Coordinate 변환, Category 검색 진행
        for (Item re : filteredRealEstate) {
            var address = buildAddress(region, re);
            var realEstateInfo = new RealEstateInfo(
                    re.getApartName(), address, type, re.getAreaForExclusiveUse(),
                    removeSpace(re.getDealAmount()), re.getBuildYear()
            );

            var coordinate =
                    kakaoMapClient.convertAddressToCoordinate(address).getDocuments().stream().findFirst().get();
            // 주변 지하철역 추가
            addSubwayList(realEstateInfo, coordinate);
            // 주변 대형마트 추가
            addSupermarketList(realEstateInfo, coordinate);

            realEstateList.getRealEstateList().add(realEstateInfo);
        }

        Collections.sort(realEstateList.getRealEstateList());

        return realEstateList;
    }

    public String removeSpace(String str) {
        return str.replaceAll(",", "").replaceAll(" ", "");
    }
    public <T extends Item> List<Item> filterByQueryParam(
            MolitRealEstateRes<T> res, Integer lowPrice, Integer highPrice, Integer lowYear, Integer highYear) {
        List<Item> filteredRealEstate = res.getBody().getItems().stream()
                .filter(item ->
                        Integer.parseInt(removeSpace(item.getDealAmount())) >= lowPrice &&
                                Integer.parseInt(removeSpace(item.getDealAmount())) <= highPrice &&
                                item.getBuildYear() >= lowYear &&
                                item.getBuildYear() <= highYear
                ).collect(Collectors.toList());

        return filteredRealEstate;
    }

    public String buildAddress(String region, Item realEstate) {
        var address = new StringBuilder(region).append(" ")
                .append(realEstate.getTownName()).append(" ")
                .append(realEstate.getLotNumber())
                .toString().replaceAll("  ", " ");

        return address;
    }

    public void addSubwayList(
            RealEstateInfo realEstateInfo, KakaoMapCoordinateRes.KakaoMapCoordinateDoc coordinate) {

        var kakaoMapCategoryRes= kakaoMapClient.searchNearby(
                "SW8", coordinate.getX(), coordinate.getY());
        for (KakaoMapCategoryRes.KakaoMapCategoryDoc sw : kakaoMapCategoryRes.getDocuments()) {
            var subway = new RealEstateInfo.Subway(
                    sw.getPlaceName(), sw.getAddressName(), sw.getPlaceUrl(), sw.getDistance()
            );
            realEstateInfo.getSubways().add(subway);
        }
    }

    public void addSupermarketList(
            RealEstateInfo realEstateInfo, KakaoMapCoordinateRes.KakaoMapCoordinateDoc coordinate) {

        var kakaoMapCategoryRes = kakaoMapClient.searchNearby(
                "MT1", coordinate.getX(), coordinate.getY()
        );
        for (KakaoMapCategoryRes.KakaoMapCategoryDoc mt : kakaoMapCategoryRes.getDocuments()) {
            var supermarket = new RealEstateInfo.Supermarket(
                    mt.getPlaceName(), mt.getAddressName(), mt.getPlaceUrl(), mt.getDistance()
            );
            realEstateInfo.getSupermarkets().add(supermarket);
        }
    }

    @Transactional
    public RealEstateInfo add(@RequestBody RealEstateInfo realEstateInfo) {

        IdGenerator<RealEstateEntity, Long> reIdGenerator = new IdGenerator<>();
        reIdGenerator.setJpaRepository(realEstateRepository);

        if (realEstateRepository.findByNameAndAddressAndTypeAndAreaForExclusiveUse(
                realEstateInfo.getName(), realEstateInfo.getAddress(),
                realEstateInfo.getType(), realEstateInfo.getAreaForExclusiveUse()) != null) {
            return null;
        }

        var realEstateEntity = realEstateInfo.toEntity();
        realEstateEntity.setId(reIdGenerator.generateId());
        realEstateEntity = realEstateRepository.save(realEstateEntity);

        if (realEstateInfo.getSubways().size() > 0) {
            saveAllRealEstateAndSubway(realEstateInfo, realEstateEntity);
        }

        if (realEstateInfo.getSupermarkets().size() > 0) {
            saveAllRealEstateAndSupermarket(realEstateInfo, realEstateEntity);
        }

        return realEstateRepository.save(realEstateEntity).toDto();
    }

    public void saveAllRealEstateAndSubway(
            RealEstateInfo realEstateInfo, RealEstateEntity realEstateEntity) {
        IdGenerator<SubwayEntity, Long> swIdGenerator = new IdGenerator<>();
        swIdGenerator.setJpaRepository(subwayRepository);
        List<SubwayEntity> subwayEntityList = new ArrayList<>();

        realEstateInfo.getSubways().stream()
                .filter(
                        sw -> subwayRepository.findByPlaceNameAndAddressName(
                                sw.getPlaceName(), sw.getAddressName()) != null
                ).forEach(
                        sw -> {
                            var subwayEntity =
                                    subwayRepository.findByPlaceNameAndAddressName(
                                            sw.getPlaceName(), sw.getAddressName()
                                    );
                            subwayEntity.updateEntity(sw);
                            associateRealEstateWithSubway(realEstateEntity, subwayEntity, sw);
                            subwayEntityList.add(subwayEntity);
                        }
                );
        realEstateInfo.getSubways().stream()
                .filter(
                        sw -> subwayRepository.findByPlaceNameAndAddressName(
                                sw.getPlaceName(), sw.getAddressName()) == null
                ).forEach(
                        sw -> {
                            var subwayEntity = sw.toEntity();
                            subwayEntity.setId(swIdGenerator.generateId());
                            subwayEntity = subwayRepository.save(subwayEntity);
                            associateRealEstateWithSubway(realEstateEntity, subwayEntity, sw);
                            subwayEntityList.add(subwayEntity);
                        }
                );

        subwayRepository.saveAll(subwayEntityList);
    }

    public void associateRealEstateWithSubway(RealEstateEntity realEstateEntity,
                                              SubwayEntity subwayEntity,
                                              RealEstateInfo.Subway subway) {
        IdGenerator<RealEstateAndSubway, Long> reswIdGenerator = new IdGenerator<>();
        reswIdGenerator.setJpaRepository(realEstateAndSubwayRepository);

        var realEstateAndSubwayEntity = RealEstateAndSubway.builder()
                .distance(subway.getDistance())
                .realEstate(realEstateEntity.combineNameAndAddress())
                .subway(subwayEntity.getPlaceName())
                .realEstateEntity(realEstateEntity)
                .subwayEntity(subwayEntity)
                .build();
        realEstateAndSubwayEntity.setId(reswIdGenerator.generateId());
        realEstateAndSubwayEntity = realEstateAndSubwayRepository.save(realEstateAndSubwayEntity);
        realEstateEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
        subwayEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
    }

    public void saveAllRealEstateAndSupermarket(
            RealEstateInfo realEstateInfo, RealEstateEntity realEstateEntity) {
        IdGenerator<SupermarketEntity, Long> mtIdGenerator = new IdGenerator<>();
        mtIdGenerator.setJpaRepository(supermarketRepository);
        List<SupermarketEntity> supermarketEntityList = new ArrayList<>();

        realEstateInfo.getSupermarkets().stream()
                .filter(
                        mt -> supermarketRepository.findByPlaceNameAndAddressName(
                                mt.getPlaceName(), mt.getAddressName()) != null
                ).forEach(
                        mt -> {
                            var supermarketEntity =
                                    supermarketRepository.findByPlaceNameAndAddressName(
                                            mt.getPlaceName(), mt.getAddressName()
                                    );
                            supermarketEntity.updateEntity(mt);
                            associateRealEstateWithSupermarket(realEstateEntity, supermarketEntity, mt);
                            supermarketEntityList.add(supermarketEntity);
                        }
                );
        realEstateInfo.getSupermarkets().stream()
                .filter(
                        mt -> supermarketRepository.findByPlaceNameAndAddressName(
                                mt.getPlaceName(), mt.getAddressName()) == null

                ).forEach(
                        mt -> {
                            var supermarketEntity = mt.toEntity();
                            supermarketEntity.setId(mtIdGenerator.generateId());
                            supermarketEntity = supermarketRepository.save(supermarketEntity);
                            associateRealEstateWithSupermarket(realEstateEntity, supermarketEntity, mt);
                            supermarketEntityList.add(supermarketEntity);
                        }
                );

        supermarketRepository.saveAll(supermarketEntityList);
    }

    public void associateRealEstateWithSupermarket(RealEstateEntity realEstateEntity,
                                                   SupermarketEntity supermarketEntity,
                                                   RealEstateInfo.Supermarket supermarket) {
        IdGenerator<RealEstateAndSupermarket, Long> remtIdGenerator = new IdGenerator<>();
        remtIdGenerator.setJpaRepository(realEstateAndSupermarketRepository);

        var realEstateAndSupermarketEntity = RealEstateAndSupermarket.builder()
                .distance(supermarket.getDistance())
                .realEstate(realEstateEntity.combineNameAndAddress())
                .supermarket(supermarketEntity.getPlaceName())
                .realEstateEntity(realEstateEntity)
                .supermarketEntity(supermarketEntity)
                .build();
        realEstateAndSupermarketEntity.setId(remtIdGenerator.generateId());
        realEstateAndSupermarketEntity = realEstateAndSupermarketRepository.save(realEstateAndSupermarketEntity);
        realEstateEntity.addRealEstateAndSupermarkets(realEstateAndSupermarketEntity);
        supermarketEntity.addRealEstateAndSupermarkets(realEstateAndSupermarketEntity);
    }

    @Transactional
    public RealEstateInfo update(@RequestBody RealEstateInfo realEstateInfo) {

        var id = realEstateInfo.getId();
        if (id == null || !realEstateRepository.existsById(id))
            return null;

        var realEstateEntity = realEstateRepository.findById(id).get();
        realEstateEntity.updateEntity(realEstateInfo);
        updateRealEstateAndSubway(realEstateInfo, realEstateEntity);
        updateRealEstateAndSupermarket(realEstateInfo, realEstateEntity);

        realEstateRepository.save(realEstateEntity);
        return realEstateEntity.toDto();
    }

    public void updateRealEstateAndSubway(RealEstateInfo realEstateInfo, RealEstateEntity realEstateEntity) {
        IdGenerator<SubwayEntity, Long> swIdGenerator = new IdGenerator<>();
        swIdGenerator.setJpaRepository(subwayRepository);
        List<RealEstateAndSubway> realEstateAndSubwayList = realEstateEntity.getRealEstateAndSubways();
        List<RealEstateAndSubway> deleteRealEstateAndSubwayList = new ArrayList<>();
        deleteRealEstateAndSubwayList.addAll(realEstateAndSubwayList);

        for (RealEstateInfo.Subway sw : realEstateInfo.getSubways()) {
            boolean find = false;
            for (RealEstateAndSubway resw : realEstateAndSubwayList) {
                var subwayEntity = resw.getSubwayEntity();
                // 업데이트 하려는 real_estate가 이미 해당 subway를 소유하고 있을 때,
                if (sw.equalsTo(subwayEntity)) {
                    subwayEntity = subwayRepository.findByPlaceNameAndAddressName(
                            subwayEntity.getPlaceName(), subwayEntity.getAddressName());
                    subwayRepository.save(subwayEntity.updateEntity(sw));

                    resw = realEstateAndSubwayRepository.findByRealEstateAndSubway(
                            realEstateInfo.combineNameAndAddress(), sw.getPlaceName());
                    realEstateAndSubwayRepository.save(resw.updateEntity(realEstateInfo, sw));

                    find = true;
                    deleteRealEstateAndSubwayList.remove(resw);
                    break;
                }
            }

            // 업데이트 하려는 real_estate가 해당 subway를 가지고 있지 않을 때,
            if (!find) {
                var subwayEntity =
                        subwayRepository.findByPlaceNameAndAddressName(sw.getPlaceName(), sw.getAddressName());
                // 해당 subway가 subway repository에 없을 때,
                if (subwayEntity == null) {
                    subwayEntity = sw.toEntity();
                    subwayEntity.setId(swIdGenerator.generateId());
                    subwayEntity = subwayRepository.save(subwayEntity);
                }

                associateRealEstateWithSubway(realEstateEntity, subwayEntity, sw);
                subwayRepository.save(subwayEntity);
            }
        }

        // 업데이트 목록에 없는 subway들을 제거
        Iterator reswIterator = deleteRealEstateAndSubwayList.iterator();
        while(reswIterator.hasNext()) {
            deleteRealEstateAndSubway(reswIterator, false);
        }

    }

    public void updateRealEstateAndSupermarket(RealEstateInfo realEstateInfo, RealEstateEntity realEstateEntity) {
        IdGenerator<SupermarketEntity, Long> mtIdGenerator = new IdGenerator<>();
        mtIdGenerator.setJpaRepository(supermarketRepository);
        List<RealEstateAndSupermarket> realEstateAndSupermarketList = realEstateEntity.getRealEstateAndSupermarkets();
        List<RealEstateAndSupermarket> deleteRealEstateAndSupermarketList = new ArrayList<>();
        deleteRealEstateAndSupermarketList.addAll(realEstateAndSupermarketList);

        for (RealEstateInfo.Supermarket mt : realEstateInfo.getSupermarkets()) {
            boolean find = false;
            for (RealEstateAndSupermarket remt : realEstateAndSupermarketList) {
                var supermarketEntity = remt.getSupermarketEntity();
                // 업데이트 하려는 real_estate가 이미 해당 supermarket를 소유하고 있을 때,
                if (mt.equalsTo(supermarketEntity)) {
                    supermarketEntity = supermarketRepository.findByPlaceNameAndAddressName(
                            supermarketEntity.getPlaceName(), supermarketEntity.getAddressName());
                    supermarketRepository.save(supermarketEntity.updateEntity(mt));

                    remt = realEstateAndSupermarketRepository.findByRealEstateAndSupermarket(
                            realEstateInfo.combineNameAndAddress(), mt.getPlaceName());
                    realEstateAndSupermarketRepository.save(remt.updateEntity(realEstateInfo, mt));

                    find = true;
                    deleteRealEstateAndSupermarketList.remove(remt);
                    break;
                }
            }

            // 업데이트 하려는 real_estate가 해당 supermarket를 가지고 있지 않을 때,
            if (!find) {
                var supermarketEntity =
                        supermarketRepository.findByPlaceNameAndAddressName(mt.getPlaceName(), mt.getAddressName());
                // 해당 supermarket가 supermarket repository에 없을 때,
                if (supermarketEntity == null) {
                    supermarketEntity = mt.toEntity();
                    supermarketEntity.setId(mtIdGenerator.generateId());
                    supermarketEntity = supermarketRepository.save(supermarketEntity);
                }

                associateRealEstateWithSupermarket(realEstateEntity, supermarketEntity, mt);
                supermarketRepository.save(supermarketEntity);
            }
        }

        // 업데이트 목록에 없는 supermarket들을 제거
        Iterator remtIterator = deleteRealEstateAndSupermarketList.iterator();
        while(remtIterator.hasNext()) {
            deleteRealEstateAndSupermarket(remtIterator, false);
        }
    }

    public List<RealEstateInfo> findList() {
        List<RealEstateInfo> realEstateInfoList = new ArrayList<>();
        realEstateRepository.findAll().forEach(ent -> realEstateInfoList.add(ent.toDto()));

        return realEstateInfoList;
    }

    @Transactional
    public void deleteAll() {
        realEstateAndSubwayRepository.deleteAll();
        subwayRepository.deleteAll();;
        realEstateAndSupermarketRepository.deleteAll();
        supermarketRepository.deleteAll();
        realEstateRepository.deleteAll();
    }

    @Transactional
    public RealEstateInfo delete(Long id) {
        if (id == null || !realEstateRepository.existsById(id))
            return null;

        var realEstateEntity = realEstateRepository.findById(id).get();
        var realEstateInfo = realEstateEntity.toDto();

        log.info("List of RealEstateAndSubway: {}", realEstateEntity.getRealEstateAndSubways());
        Iterator reswIterator = realEstateEntity.getRealEstateAndSubways().iterator();
        while (reswIterator.hasNext()) {
            deleteRealEstateAndSubway(reswIterator, true);
        }
        log.info("List of RealEstateAndSupermarket: {}", realEstateEntity.getRealEstateAndSubways());
        Iterator remtIterator = realEstateEntity.getRealEstateAndSupermarkets().iterator();
        while (remtIterator.hasNext()) {
            deleteRealEstateAndSupermarket(remtIterator, true);
        }

        realEstateRepository.delete(realEstateEntity);
        return realEstateInfo;
    }

    public void deleteRealEstateAndSubway(Iterator iter, boolean selfLoop) {
        var realEstateAndSubway = (RealEstateAndSubway)iter.next();
        var subwayEntity = realEstateAndSubway.getSubwayEntity();

        if (selfLoop)
            iter.remove();
        else
            realEstateAndSubway.getRealEstateEntity().deleteRealEstateAndSubway(realEstateAndSubway);
        subwayEntity.deleteRealEstateAndSubway(realEstateAndSubway);
        realEstateAndSubwayRepository.delete(realEstateAndSubway);

        if (subwayEntity.getRealEstateAndSubways().isEmpty()) {
            subwayRepository.delete(subwayEntity);
        }
    }

    public void deleteRealEstateAndSupermarket(Iterator iter, boolean selfLoop) {
        var realEstateAndSupermarket = (RealEstateAndSupermarket)iter.next();
        var supermarketEntity = realEstateAndSupermarket.getSupermarketEntity();

        if (selfLoop)
            iter.remove();
        else
            realEstateAndSupermarket.getRealEstateEntity().deleteRealEstateAndSupermarket(realEstateAndSupermarket);
        supermarketEntity.deleteRealEstateAndSupermarket(realEstateAndSupermarket);
        realEstateAndSupermarketRepository.delete(realEstateAndSupermarket);

        if (supermarketEntity.getRealEstateAndSupermarkets().isEmpty())
            supermarketRepository.delete(supermarketEntity);
    }

}
