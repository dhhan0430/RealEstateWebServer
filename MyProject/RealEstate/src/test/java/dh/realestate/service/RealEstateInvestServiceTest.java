package dh.realestate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RealEstateInvestServiceTest {

    @Autowired
    private RealEstateInvestService realEstateInvestService;

    @Test
    public void realEstateSearchTest() throws FileNotFoundException, UnsupportedEncodingException {

        var realEstateSearchDto = realEstateInvestService.search(
                "서초구", "아파트", 100000, 500000, 1950, 2021
        );
        System.out.println(realEstateSearchDto);
    }
}