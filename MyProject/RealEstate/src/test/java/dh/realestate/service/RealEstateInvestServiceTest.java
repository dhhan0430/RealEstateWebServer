package dh.realestate.service;

import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RealEstateInvestServiceTest {

    @Autowired
    private RealEstateInvestService realEstateInvestService;

    @Autowired
    private RealEstateRepository realEstateRepository;
    @Autowired
    private RealEstateAndSubwayRepository realEstateAndSubwayRepository;
    @Autowired
    private SubwayRepository subwayRepository;
    @Autowired
    private RealEstateAndSupermarketRepository realEstateAndSupermarketRepository;
    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void realEstateSearchTest() throws FileNotFoundException, UnsupportedEncodingException {

        var realEstateList = realEstateInvestService.search(
                "서초구", "아파트", 100000, 500000, 1950, 2021
        );
        System.out.println(realEstateList);
    }

    @Transactional
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
        var realEstateInfoOutput =
                realEstateInvestService.add(realEstateInfoInput);
        System.out.println("/-----------------------------------------------------------------------------");
        System.out.println("*add 1: " + realEstateInfoOutput);
        System.out.println("*RealEstate Repo: "); realEstateRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSubway Repo: "); realEstateAndSubwayRepository.findAll().forEach(System.out::println);
        System.out.println("*Subway Repo: "); subwayRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSupermarket Repo: "); realEstateAndSupermarketRepository.findAll().forEach(System.out::println);
        System.out.println("*Supermarket Repo: "); supermarketRepository.findAll().forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------------------/");


        var realEstateInfoInput2 = new RealEstateInfo(
                "반포자이", "서초구 반포동 20-43", "아파트",
                84.943, "390000", 2009
        );
        realEstateInfoInput2.getSubways().add(
                new RealEstateInfo.Subway(
                        "반포역 7호선",
                        "서울 서초구 잠원동 103",
                        "http://place.map.kakao.com/21160707",
                        "206"
                )
        );
        realEstateInfoInput2.getSubways().add(
                new RealEstateInfo.Subway(
                        "사평역 9호선",
                        "서울 서초구 반포동 128-7",
                        "http://place.map.kakao.com/7918693",
                        "442"
                )
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "노브랜드 강남터미널점",
                        "서울 서초구 반포동 19-4",
                        "http://place.map.kakao.com/914807858",
                        "360"
                )
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "킴스클럽 강남점",
                        "서울 서초구 잠원동 70-2",
                        "http://place.map.kakao.com/18276130",
                        "451"
                )
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "홈플러스익스프레스 반포점",
                        "서울 서초구 반포동 20-51",
                        "http://place.map.kakao.com/11779677",
                        "258"
                )
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "GS더프레시 서초점",
                        "서울 서초구 반포동 30-25",
                        "http://place.map.kakao.com/21308698",
                        "396"
                )
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "롯데슈퍼 잠원점",
                        "서울 서초구 잠원동 58-24",
                        "http://place.map.kakao.com/10363444",
                        "248"
                )
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "롯데슈퍼 반포2점",
                        "서울 서초구 반포동 54-1",
                        "http://place.map.kakao.com/707649582",
                        "420"
                )
        );
        var realEstateInfoOutput2 =
                realEstateInvestService.add(realEstateInfoInput2);
        System.out.println("/-----------------------------------------------------------------------------");
        System.out.println("*add 2: " + realEstateInfoOutput2);
        System.out.println("*RealEstate Repo: "); realEstateRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSubway Repo: "); realEstateAndSubwayRepository.findAll().forEach(System.out::println);
        System.out.println("*Subway Repo: "); subwayRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSupermarket Repo: "); realEstateAndSupermarketRepository.findAll().forEach(System.out::println);
        System.out.println("*Supermarket Repo: "); supermarketRepository.findAll().forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------------------/");


        var realEstateInfoInput3 = new RealEstateInfo(
                "반포래미안아이파크", "서초구 반포동 1341", "아파트",
                99.92, "397000", 2018
        );
        realEstateInfoInput3.getSubways().add(
                new RealEstateInfo.Subway(
                        "사평역 9호선",
                        "서울 서초구 반포동 128-7",
                        "http://place.map.kakao.com/7918693",
                        "348"
                )
        );
        realEstateInfoInput3.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "홈플러스익스프레스 서초점",
                        "서울 서초구 서초동 1685-3",
                        "http://place.map.kakao.com/10979066",
                        "258"
                )
        );
        realEstateInfoInput3.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "GS더프레시 서초점",
                        "서울 서초구 반포동 30-25",
                        "http://place.map.kakao.com/21308698",
                        "179"
                )
        );
        realEstateInfoInput3.getSupermarkets().add(
                new RealEstateInfo.Supermarket(
                        "롯데슈퍼 반포2점",
                        "서울 서초구 반포동 54-1",
                        "http://place.map.kakao.com/707649582",
                        "250"
                )
        );
        var realEstateInfoOutput3 =
                realEstateInvestService.add(realEstateInfoInput3);
        System.out.println("/-----------------------------------------------------------------------------");
        System.out.println("*add 3: " + realEstateInfoOutput3);
        System.out.println("*RealEstate Repo: "); realEstateRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSubway Repo: "); realEstateAndSubwayRepository.findAll().forEach(System.out::println);
        System.out.println("*Subway Repo: "); subwayRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSupermarket Repo: "); realEstateAndSupermarketRepository.findAll().forEach(System.out::println);
        System.out.println("*Supermarket Repo: "); supermarketRepository.findAll().forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------------------/");


    }

}