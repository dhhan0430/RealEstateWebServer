package dh.realestate.service;

import dh.realestate.model.dto.RealEstateInfoDto;
import dh.realestate.model.dto.RealEstateSearchDto;
import dh.realestate.service.kakaomap.KakaoMapClient;
import dh.realestate.service.kakaomap.dto.KakaoMapCategoryRes;
import dh.realestate.service.kakaomap.dto.KakaoMapCoordinateRes;
import dh.realestate.service.molit.MolitClient;
import dh.realestate.service.molit.dto.MolitRealEstateRes;
import dh.realestate.service.molit.dto.xmlresponse.Item;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public RealEstateSearchDto search(
            String region, String type, Integer lowPrice, Integer highPrice, Integer lowYear, Integer highYear)
            throws FileNotFoundException, UnsupportedEncodingException {

        // 예외 처리

        // input parameter: 1.region, 2.type
        var molitRealEstateRes=
                molitClient.searchRealEstate(region, type);

        // query parameter 기준에 의한 filtering 진행. lowPrice <= price <= highPrice, lowYear <= year <= highYear
        List<Item> filteredRealEstate = filterByQueryParam(
                molitRealEstateRes, lowPrice, highPrice, lowYear, highYear
        );

        var realEstateSearchDto = new RealEstateSearchDto(
                region, type, currentYear + " " + currentMonth,
                new StringBuilder().append(lowPrice).append("~").append(highPrice).toString(),
                new StringBuilder().append(lowYear).append("~").append(highYear).toString(),
                (int)filteredRealEstate.stream().count()
        );

        // filtering 된 매물들 기준으로 KakaoMap Coordinate 변환 진행
        for (Item re : filteredRealEstate) {
            var address = buildAddress(region, re);
            var realEstateInfoDto = new RealEstateInfoDto(
                    re.getApartName(), address, type, re.getAreaForExclusiveUse(),
                    removeSpace(re.getDealAmount()), re.getBuildYear()
            );

            var coordinate =
                    kakaoMapClient.convertAddressToCoordinate(address).getDocuments().stream().findFirst().get();
            // 주변 지하철역 추가
            addSubwayList(realEstateInfoDto, coordinate);
            // 주변 대형마트 추가
            addSupermarketList(realEstateInfoDto, coordinate);

            realEstateSearchDto.getRealEstateList().add(realEstateInfoDto);
        }

        Collections.sort(realEstateSearchDto.getRealEstateList());

        return realEstateSearchDto;
    }

    public String removeSpace(String str) {
        return str.replaceAll(",", "").replaceAll(" ", "");
    }
    public List<Item> filterByQueryParam(
            MolitRealEstateRes res, Integer lowPrice, Integer highPrice, Integer lowYear, Integer highYear) {
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
            RealEstateInfoDto realEstateInfoDto, KakaoMapCoordinateRes.KakaoMapCoordinateDoc coordinate) {

        var kakaoMapCategoryRes= kakaoMapClient.searchNearby(
                "SW8", coordinate.getX(), coordinate.getY());
        for (KakaoMapCategoryRes.KakaoMapCategoryDoc sw : kakaoMapCategoryRes.getDocuments()) {
            var subway = new RealEstateInfoDto.Subway(
                    sw.getPlaceName(), sw.getAddressName(), sw.getPlaceUrl(), sw.getDistance()
            );
            realEstateInfoDto.getSubways().add(subway);
        }
    }

    public void addSupermarketList(
            RealEstateInfoDto realEstateInfoDto, KakaoMapCoordinateRes.KakaoMapCoordinateDoc coordinate) {

        var kakaoMapCategoryRes = kakaoMapClient.searchNearby(
                "MT1", coordinate.getX(), coordinate.getY()
        );
        for (KakaoMapCategoryRes.KakaoMapCategoryDoc mt : kakaoMapCategoryRes.getDocuments()) {
            var supermarket = new RealEstateInfoDto.Supermarket(
                    mt.getPlaceName(), mt.getAddressName(), mt.getPlaceUrl(), mt.getDistance()
            );
            realEstateInfoDto.getSupermarkets().add(supermarket);
        }
    }

}
