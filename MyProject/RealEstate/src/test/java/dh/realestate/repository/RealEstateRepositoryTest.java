package dh.realestate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RealEstateRepositoryTest {

    @Autowired
    private RealEstateRepository realEstateRepository;
    @Autowired
    private SubwayRepository subwayRepository;
    @Autowired
    private SupermarketRepository supermarketRepository;


    @Test
    void dataSqlTest() {

//        var user = new User();
//        System.out.println(user);
//        userRepository.save(user);
//
//        System.out.println("-----------------------------------------");
//        userRepository.findAll().forEach(System.out::println);
//        System.out.println("-----------------------------------------");

        System.out.println("-------------------------------------------");
        realEstateRepository.findAll().forEach(System.out::println);
        System.out.println("-------------------------------------------");

    }

}