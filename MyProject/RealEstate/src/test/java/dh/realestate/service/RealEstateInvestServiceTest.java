package dh.realestate.service;

import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.model.entity.RealEstateAndSubway;
import dh.realestate.model.entity.RealEstateEntity;
import dh.realestate.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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

        var realEstateInfoInput1 = new RealEstateInfo("래미안퍼스티지", "서초구 반포동 18-1", "아파트", 84.93, "390000", 2009);
        realEstateInfoInput1.getSubways().add(
                new RealEstateInfo.Subway("신반포역 9호선", "서울 서초구 반포동 128-1", "http://place.map.kakao.com/7968042", "286")
        );
        realEstateInfoInput1.getSupermarkets().add(
                new RealEstateInfo.Supermarket("홈플러스익스프레스 반포2점", "서울 서초구 반포동 18-3", "http://place.map.kakao.com/15523355", "229")
        );
        realEstateInfoInput1.getSupermarkets().add(
                new RealEstateInfo.Supermarket("롯데슈퍼 신반포점", "서울 서초구 반포동 2-8", "http://place.map.kakao.com/7968042", "286")
        );
        var realEstateInfoOutput1 = realEstateInvestService.add(realEstateInfoInput1);
        printAll(realEstateInfoOutput1, 1);


        var realEstateInfoInput2 = new RealEstateInfo("반포자이", "서초구 반포동 20-43", "아파트", 84.943, "390000", 2009);
        realEstateInfoInput2.getSubways().add(
                new RealEstateInfo.Subway("반포역 7호선", "서울 서초구 잠원동 103", "http://place.map.kakao.com/21160707", "206")
        );
        realEstateInfoInput2.getSubways().add(
                new RealEstateInfo.Subway("사평역 9호선", "서울 서초구 반포동 128-7", "http://place.map.kakao.com/7918693", "442")
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket("노브랜드 강남터미널점", "서울 서초구 반포동 19-4", "http://place.map.kakao.com/914807858", "360")
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket("킴스클럽 강남점", "서울 서초구 잠원동 70-2", "http://place.map.kakao.com/18276130", "451")
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket("홈플러스익스프레스 반포점", "서울 서초구 반포동 20-51", "http://place.map.kakao.com/11779677", "258")
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket("GS더프레시 서초점", "서울 서초구 반포동 30-25", "http://place.map.kakao.com/21308698", "396")
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket("롯데슈퍼 잠원점", "서울 서초구 잠원동 58-24", "http://place.map.kakao.com/10363444", "248")
        );
        realEstateInfoInput2.getSupermarkets().add(
                new RealEstateInfo.Supermarket("롯데슈퍼 반포2점", "서울 서초구 반포동 54-1", "http://place.map.kakao.com/707649582", "420")
        );
        var realEstateInfoOutput2 =
                realEstateInvestService.add(realEstateInfoInput2);
        printAll(realEstateInfoOutput2, 2);

        var realEstateInfoInput3 = new RealEstateInfo("반포래미안아이파크", "서초구 반포동 1341", "아파트", 99.92, "397000", 2018);
        realEstateInfoInput3.getSubways().add(
                new RealEstateInfo.Subway("사평역 9호선", "서울 서초구 반포동 128-7", "http://place.map.kakao.com/7918693", "348")
        );
        realEstateInfoInput3.getSupermarkets().add(
                new RealEstateInfo.Supermarket("홈플러스익스프레스 서초점", "서울 서초구 서초동 1685-3", "http://place.map.kakao.com/10979066", "258")
        );
        realEstateInfoInput3.getSupermarkets().add(
                new RealEstateInfo.Supermarket("GS더프레시 서초점", "서울 서초구 반포동 30-25", "http://place.map.kakao.com/21308698", "179")
        );
        realEstateInfoInput3.getSupermarkets().add(
                new RealEstateInfo.Supermarket("롯데슈퍼 반포2점", "서울 서초구 반포동 54-1", "http://place.map.kakao.com/707649582", "250")
        );
        var realEstateInfoOutput3 =
                realEstateInvestService.add(realEstateInfoInput3);
        printAll(realEstateInfoOutput3, 3);
    }

    public void printAll(RealEstateInfo realEstateInfo, int idx) {
        System.out.println("/-----------------------------------------------------------------------------");
        System.out.println("*add " + idx + ": " + realEstateInfo);
        System.out.println("*RealEstate Repo: "); realEstateRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSubway Repo: "); realEstateAndSubwayRepository.findAll().forEach(System.out::println);
        System.out.println("*Subway Repo: "); subwayRepository.findAll().forEach(System.out::println);
        System.out.println("*RealEstateAndSupermarket Repo: "); realEstateAndSupermarketRepository.findAll().forEach(System.out::println);
        System.out.println("*Supermarket Repo: "); supermarketRepository.findAll().forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------------------/");
    }

    @Test
    @Transactional
    public void relationDeleteTest() {

        var realEstateEntity1 = new RealEstateEntity();
        realEstateEntity1.setName("부동산1");

        var realEstateAndSubway1 = new RealEstateAndSubway();
        realEstateAndSubway1.setRealEstate("부동산1");
        realEstateAndSubway1.setSubway("신논현역 9호선");
        realEstateAndSubway1.setRealEstateEntity(realEstateEntity1);
        realEstateAndSubway1 = realEstateAndSubwayRepository.save(realEstateAndSubway1);

        var realEstateAndSubway2 = new RealEstateAndSubway();
        realEstateAndSubway2.setRealEstate("부동산2");
        realEstateAndSubway2.setSubway("신논현역 신분당선");
        realEstateAndSubway2.setRealEstateEntity(realEstateEntity1);
        realEstateAndSubway2 = realEstateAndSubwayRepository.save(realEstateAndSubway2);

        var realEstateAndSubway3 = new RealEstateAndSubway();
        realEstateAndSubway3.setRealEstate("부동산3");
        realEstateAndSubway3.setSubway("논현역 7호선");
        realEstateAndSubway3.setRealEstateEntity(realEstateEntity1);
        realEstateAndSubway3 = realEstateAndSubwayRepository.save(realEstateAndSubway3);

        var realEstateAndSubway4 = new RealEstateAndSubway();
        realEstateAndSubway4.setRealEstate("부동산4");
        realEstateAndSubway4.setSubway("논현역 신분당선");
        realEstateAndSubway4.setRealEstateEntity(realEstateEntity1);
        realEstateAndSubway4 = realEstateAndSubwayRepository.save(realEstateAndSubway4);

        realEstateEntity1.addRealEstateAndSubways(realEstateAndSubway1, realEstateAndSubway2,
                realEstateAndSubway3, realEstateAndSubway4);
        realEstateRepository.save(realEstateEntity1);


        boolean check[] = new boolean[4];
        Arrays.fill(check, false);
        check[1] = true; check[3] = true;
        var cnt = realEstateEntity1.getRealEstateAndSubways().size();
        realEstateEntity1.getRealEstateAndSubways().stream().forEach(System.out::println);
        System.out.println("-----------------------------------------------------");

        for (int i=cnt-1; i>=0; i--) {
            System.out.println("current idx: " + i);
            if (check[i] == false) {
                System.out.println("delete idx: " + i);
                var entity = realEstateEntity1.getRealEstateAndSubways().get(i);
                //entity.setRealEstateEntity(null);
                realEstateAndSubwayRepository.delete(entity);
                System.out.println("delete: " + entity);
                realEstateEntity1.getRealEstateAndSubways().remove(entity);
                //entityManager.flush();
            }

            realEstateEntity1.getRealEstateAndSubways().stream().forEach(System.out::println);
            for (int j = 0; j < realEstateEntity1.getRealEstateAndSubways().size(); j++) {
                System.out.println("idx(" + j + "): " + realEstateEntity1.getRealEstateAndSubways().get(j));
            }
            System.out.println("-----------------------------------------------------");

        }
        entityManager.flush();

        System.out.println("findAll():");
        realEstateAndSubwayRepository.findAll().forEach(System.out::println);
        System.out.println("-----------------------------------------------------");

        var realEstateEntity_found = realEstateRepository.findById(1L).get();
        realEstateEntity_found.getRealEstateAndSubways().stream().forEach(System.out::println);
        System.out.println("-----------------------------------------------------");


    }

}