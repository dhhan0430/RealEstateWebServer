package dh.realestate.service;

import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.model.dto.RealEstateSearch;

import dh.realestate.repository.RealEstateRepository;
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

    public RealEstateSearch search(
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

        var realEstateSearch = new RealEstateSearch(
                region, type, currentYear + " " + currentMonth,
                new StringBuilder().append(lowPrice).append("~").append(highPrice).toString(),
                new StringBuilder().append(lowYear).append("~").append(highYear).toString(),
                (int)filteredRealEstate.stream().count()
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

            realEstateSearch.getRealEstateList().add(realEstateInfo);
        }

        Collections.sort(realEstateSearch.getRealEstateList());

        return realEstateSearch;
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


    public void add(@RequestBody RealEstateInfo realEstateInfo) {


    }

}
