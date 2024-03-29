package dh.realestate.service.molit;

import dh.realestate.service.molit.dto.MolitRealEstateRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

@SpringBootTest
class MolitClientTest {

    @Autowired
    private MolitClient molitClient;

    @Test
    public void molitCodeTest() throws FileNotFoundException, UnsupportedEncodingException {

        System.out.println(molitClient.searchCode("서초구"));
    }


    @Test
    public void molitRealEstateTest() throws FileNotFoundException, UnsupportedEncodingException {

        MolitRealEstateRes result = molitClient.searchRealEstate("서초구", "아파트");
        System.out.println(result);
    }

}