package dh.realestate.service.molit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

@SpringBootTest
class MolitCodeTest {


    @Test
    public void molitCodeTest() throws FileNotFoundException, UnsupportedEncodingException {

        MolitCode.codeSearch("서초구");
    }
}