package dh.realestate.service;

import dh.realestate.model.dto.RealEstateInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RealEstateInvestServiceTest {

    @Autowired
    private RealEstateInvestService realEstateInvestService;

    @Test
    public void realEstateSearchTest() throws FileNotFoundException, UnsupportedEncodingException {

        var realEstateSearch = realEstateInvestService.search(
                "서초구", "아파트", 100000, 500000, 1950, 2021
        );
        System.out.println(realEstateSearch);
    }

    @Test
    public void realEstateAddTest() {

        var realEstateInfoInput = new RealEstateInfo(
            "래미안퍼스티지", "서초구 반포동 18-1", "아파트",
                84.93, "390000", 2009
        );
        realEstateInfoInput.getSubways().add(
                new RealEstateInfo.Subway(
                        "신반포역 9호선",
                        "서울 서초구 반포동 128-1",
                        "http://place.map.kakao.com/7968042",
                        "286"
                )
        );
        realEstateInfoInput.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "홈플러스익스프레스 반포2점",
                        "서울 서초구 반포동 18-3",
                        "http://place.map.kakao.com/15523355",
                        "229"
                )
        );
        realEstateInfoInput.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "롯데슈퍼 신반포점",
                        "서울 서초구 반포동 2-8",
                        "http://place.map.kakao.com/7968042",
                        "286"
                )
        );
        var realEstateInfoOutput = realEstateInvestService.add(
                realEstateInfoInput
        );
        System.out.println("result: " + realEstateInfoOutput);
    }

}