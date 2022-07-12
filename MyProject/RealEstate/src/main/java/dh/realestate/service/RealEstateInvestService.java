package dh.realestate.service;

import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.model.dto.RealEstateList;

import dh.realestate.model.entity.*;
import dh.realestate.repository.*;
import dh.realestate.service.kakaomap.KakaoMapClient;
import dh.realestate.service.kakaomap.dto.KakaoMapCategoryRes;
import dh.realestate.service.kakaomap.dto.KakaoMapCoordinateRes;
import dh.realestate.service.molit.MolitClient;
import dh.realestate.service.molit.dto.MolitRealEstateRes;
import dh.realestate.service.molit.dto.xmlresponse.body.item.Item;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public RealEstateInfo add(@RequestBody RealEstateInfo realEstateInfo) {

        if (realEstateRepository.findByNameAndAddressAndTypeAndAreaForExclusiveUse(
                realEstateInfo.getName(), realEstateInfo.getAddress(),
                realEstateInfo.getType(), realEstateInfo.getAreaForExclusiveUse()) != null) {
            return null;
        }

        var realEstateEntity = realEstateRepository.save(
                realEstateInfo.toEntity()
        );

        if (realEstateInfo.getSubways().size() > 0) {
            
            var subwayEntityList =
                    associateRealEstateWithSubway(realEstateInfo, realEstateEntity);
            subwayRepository.saveAll(subwayEntityList);
        }

        if (realEstateInfo.getSupermarkets().size() > 0) {

            var supermarketEntityList =
                    associateRealEstateWithSupermarket(realEstateInfo, realEstateEntity);
            supermarketRepository.saveAll(supermarketEntityList);
        }

        return realEstateRepository.save(realEstateEntity).toDto();
    }

    public List<SubwayEntity> associateRealEstateWithSubway(
            RealEstateInfo realEstateInfo, RealEstateEntity realEstateEntity) {

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
                            var realEstateAndSubwayEntity =
                                    realEstateAndSubwayRepository.save(
                                            new RealEstateAndSubway(
                                                    sw.getDistance(),
                                                    realEstateEntity.combineNameAndAddress(),
                                                    subwayEntity.getPlaceName(),
                                                    realEstateEntity, subwayEntity
                                            )
                                    );
                            realEstateEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
                            subwayEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
                            subwayEntityList.add(subwayEntity);
                        }
                );
        realEstateInfo.getSubways().stream()
                .filter(
                        sw -> subwayRepository.findByPlaceNameAndAddressName(
                                sw.getPlaceName(), sw.getAddressName()) == null
                ).forEach(
                        sw -> {
                            var subwayEntity = subwayRepository.save(sw.toEntity());
                            var realEstateAndSubwayEntity =
                                    realEstateAndSubwayRepository.save(
                                            new RealEstateAndSubway(
                                                    sw.getDistance(),
                                                    realEstateEntity.combineNameAndAddress(),
                                                    subwayEntity.getPlaceName(),
                                                    realEstateEntity, subwayEntity
                                            )
                                    );
                            realEstateEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
                            subwayEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
                            subwayEntityList.add(subwayEntity);
                        }
                );

        return subwayEntityList;
    }

    public List<SupermarketEntity> associateRealEstateWithSupermarket(
            RealEstateInfo realEstateInfo, RealEstateEntity realEstateEntity) {

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
                            var realEstateAndSupermarketEntity =
                                    realEstateAndSupermarketRepository.save(
                                            new RealEstateAndSupermarket(
                                                    mt.getDistance(),
                                                    realEstateEntity.combineNameAndAddress(),
                                                    supermarketEntity.getPlaceName(),
                                                    realEstateEntity, supermarketEntity
                                            )
                                    );
                            realEstateEntity.addRealEstateAndSupermarkets(realEstateAndSupermarketEntity);
                            supermarketEntity.addRealEstateAndSupermarkets(realEstateAndSupermarketEntity);
                            supermarketEntityList.add(supermarketEntity);
                        }
                );
        realEstateInfo.getSupermarkets().stream()
                .filter(
                        mt -> supermarketRepository.findByPlaceNameAndAddressName(
                                mt.getPlaceName(), mt.getAddressName()) == null

                ).forEach(
                        mt -> {
                            var supermarketEntity = supermarketRepository.save(mt.toEntity());
                            var supermarketAndSupermarketEntity =
                                    realEstateAndSupermarketRepository.save(
                                            new RealEstateAndSupermarket(
                                                    mt.getDistance(),
                                                    realEstateEntity.combineNameAndAddress(),
                                                    supermarketEntity.getPlaceName(),
                                                    realEstateEntity, supermarketEntity
                                            )
                                    );
                            realEstateEntity.addRealEstateAndSupermarkets(supermarketAndSupermarketEntity);
                            supermarketEntity.addRealEstateAndSupermarkets(supermarketAndSupermarketEntity);
                            supermarketEntityList.add(supermarketEntity);
                        }
                );

        return supermarketEntityList;
    }

    public RealEstateInfo update(@RequestBody RealEstateInfo realEstateInfo) {

        var id = realEstateInfo.getId();
        if (id == null || !realEstateRepository.existsById(id))
            return null;

        var realEstateEntity = realEstateRepository.findById(id).get();
        realEstateEntity.updateEntity(realEstateInfo);
        updateRealEstateAndSubway(realEstateEntity, realEstateInfo, realEstateEntity.getRealEstateAndSubways());

        realEstateRepository.save(realEstateEntity);

        return null;
    }

    public void updateRealEstateAndSubway(RealEstateEntity realEstateEntity,
                                          RealEstateInfo realEstateInfo,
                                          List<RealEstateAndSubway> realEstateAndSubwayList) {

        var entityReswCnt = realEstateAndSubwayList.size();
        boolean[] updateCheck = new boolean[entityReswCnt];
        Arrays.fill(updateCheck, false);
        Long[] realEstateAndSubwyId = new Long[entityReswCnt];
        for (int i = 0; i < entityReswCnt; i++)
            realEstateAndSubwyId[i] = realEstateAndSubwayList.get(i).getId();

        for (RealEstateInfo.Subway sw : realEstateInfo.getSubways()) {
            boolean find = false;

            for (int i = 0; i < entityReswCnt; i++) {
                var realEstateAndSubway = realEstateAndSubwayList.get(i);
                var subwayEntity = realEstateAndSubway.getSubwayEntity();
                // 업데이트 하려는 real_estate가 이미 해당 subway를 소유하고 있을 때,
                if (sw.getPlaceName().equals(subwayEntity.getPlaceName())
                        && sw.getAddressName().equals(subwayEntity.getAddressName())) {
                    subwayEntity = subwayRepository.findByPlaceNameAndAddressName(
                            subwayEntity.getPlaceName(), subwayEntity.getAddressName());
                    subwayEntity.updateEntity(sw);
                    subwayRepository.save(subwayEntity);

                    realEstateAndSubway = realEstateAndSubwayRepository.findByRealEstateAndSubway(
                            realEstateInfo.getName(), sw.getPlaceName());
                    realEstateAndSubway.updateEntity(realEstateInfo.getName(), sw);
                    realEstateAndSubwayRepository.save(realEstateAndSubway);

                    find = true;
                    updateCheck[i] = true;
                    break;
                }
            }

            // 업데이트 하려는 real_estate가 해당 subway를 가지고 있지 않을 때,
            if (!find) {
                var subwayEntity =
                        subwayRepository.findByPlaceNameAndAddressName(sw.getPlaceName(), sw.getAddressName());
                // 해당 subway가 subway repository에 없을 때,
                if (subwayEntity == null) {
                    subwayEntity = subwayRepository.save(sw.toEntity());
                }

                var realEstateAndSubwayEntity =
                        realEstateAndSubwayRepository.save(
                                new RealEstateAndSubway(
                                        sw.getDistance(),
                                        realEstateEntity.combineNameAndAddress(),
                                        subwayEntity.getPlaceName(),
                                        realEstateEntity, subwayEntity
                                )
                        );
                realEstateEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
                subwayEntity.addRealEstateAndSubways(realEstateAndSubwayEntity);
                subwayRepository.save(subwayEntity);
            }

        }

        for (int i = 0; i < entityReswCnt; i++) {
            if (!updateCheck[i])
                deleteRealEstateAndSubway(id??);
        }

    }

    public List<RealEstateInfo> findList() {

        List<RealEstateInfo> realEstateInfoList = new ArrayList<>();
        realEstateRepository.findAll().forEach(ent -> realEstateInfoList.add(ent.toDto()));

        return realEstateInfoList;
    }

    public RealEstateInfo delete(Long id) {

        if (id == null || !realEstateRepository.existsById(id))
            return null;

        var realEstateEntity = realEstateRepository.findById(id).get();
        var realEstateInfo = realEstateEntity.toDto();

        for (RealEstateAndSubway resw : realEstateEntity.getRealEstateAndSubways()) {
            deleteRealEstateAndSubway(resw);
        }
        for (RealEstateAndSupermarket remt : realEstateEntity.getRealEstateAndSupermarkets()) {
            deleteRealEstateAndSupermarket(remt);
        }

        realEstateRepository.delete(realEstateEntity);

        return realEstateInfo;
    }


    public void deleteRealEstateAndSubway(RealEstateAndSubway realEstateAndSubway) {
        var subwayEntityId = realEstateAndSubway.getSubwayEntity().getId();
        realEstateAndSubwayRepository.delete(realEstateAndSubway);
        if (subwayRepository.findById(subwayEntityId).get().getRealEstateAndSubways().isEmpty())
            subwayRepository.deleteById(subwayEntityId);
    }

    public void deleteRealEstateAndSupermarket(RealEstateAndSupermarket realEstateAndSupermarket) {
        var supermarketId = realEstateAndSupermarket.getSupermarketEntity().getId();
        realEstateAndSupermarketRepository.delete(realEstateAndSupermarket);
        if(supermarketRepository.findById(supermarketId).get().getRealEstateAndSupermarkets().isEmpty())
            supermarketRepository.deleteById(supermarketId);
    }





}
