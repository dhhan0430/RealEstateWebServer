package dh.realestate.service.kakaomap;

import org.hibernate.dialect.pagination.SQL2008StandardLimitHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KakaoMapClientTest {

    @Autowired
    private KakaoMapClient kakaoMapClient;

    @Test
    public void kakaoMapCoordinateTest() {

        System.out.println(
                kakaoMapClient.convertAddressToCoordinate("서초구 반포동 757")
        );
    }

    @Test
    public void kakaoMapSearchNearbyTest() {

        System.out.println(
                kakaoMapClient.searchNearby(
                        "SW8",
                        "127.06283102249932",
                        "37.514322572335935"
                        )
        );
    }

}