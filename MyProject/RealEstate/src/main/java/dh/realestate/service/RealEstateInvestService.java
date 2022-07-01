package dh.realestate.service;

import dh.realestate.model.dto.RealEstateDto;
import dh.realestate.service.kakaomap.KakaoMapClient;
import dh.realestate.service.molit.MolitClient;
import dh.realestate.service.molit.dto.MolitRealEstateRes;
import dh.realestate.service.molit.dto.xmlresponse.Item;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealEstateInvestService {

    @Value("${KakaoMap.CurrentYear}")
    private String currentYear;

    private final MolitClient molitClient;
    private final KakaoMapClient kakaoMapClient;

    public /*RealEstateDto*/ void search(String region,
                                String type,
                                Integer lowPrice,
                                Integer highPrice,
                                Integer lowYear,
                                Integer highYear)
            throws FileNotFoundException, UnsupportedEncodingException {

        // 예외 처리

        // input parameter: 1.region, 2.type
        var molitRealEstateRes=
                molitClient.searchRealEstate(region, type);

        // rest api query parameter 기준에 의한 filtering 진행.
        // lowPrice <= price <= highPrice, lowYear <= year <= highYear
        List<Item> filteredRealEstate = molitRealEstateRes
                .getBody().getItems().stream()
                .filter(item ->
                        Integer.parseInt(item.getDealAmount()
                                .replaceAll(",", "")
                                .replaceAll(" ", "")) >= lowPrice &&
                        Integer.parseInt(item.getDealAmount()
                                .replaceAll(",", "")
                                .replaceAll(" ", "")) <= highPrice &&
                        item.getBuildYear() >= lowYear &&
                        item.getBuildYear() <= highYear
                ).collect(Collectors.toList());

        System.out.println("total count: " + filteredRealEstate.stream().count());

        // filtering 된 매물들 기준으로 KakaoMap Coordinate 변환 진행
        for (Item realEstate : filteredRealEstate) {

            System.out.println("-------------------------------------------------------------------------");
            var address = new StringBuilder(region).append(" ")
                    .append(realEstate.getTownName()).append(" ")
                    .append(realEstate.getLotNumber())
                    .toString();

            var kakaoMapCoordinateRes
                    = kakaoMapClient.convertAddressToCoordinate(address);

            var coordinate =
                    kakaoMapCoordinateRes.getDocuments().stream()
                            .findFirst().get();

            var kakaoMapCategoryRes=
                    kakaoMapClient.searchNearby(
                            "SW8",
                            coordinate.getX(),
                            coordinate.getY()
                    );

            System.out.println("address: " + address + " " + realEstate.getApartName());
            System.out.println("total cnt: " + kakaoMapCategoryRes.getMeta().getTotalCount() + ", " + kakaoMapCategoryRes);

        }

    }

}
