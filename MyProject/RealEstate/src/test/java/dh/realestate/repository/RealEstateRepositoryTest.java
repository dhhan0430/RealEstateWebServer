package dh.realestate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

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

    }

}