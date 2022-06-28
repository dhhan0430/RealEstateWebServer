package dh.realestate.service.molit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MolitClientTest {

    @Autowired
    private MolitClient molitClient;

    @Test
    public void molitServiceTest() throws FileNotFoundException, UnsupportedEncodingException {

        molitClient.searchRealEstate("서초구", "아파트");
    }

}