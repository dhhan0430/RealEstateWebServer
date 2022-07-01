package dh.realestate.service.kakaomap.url;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMapCoordinateUrl implements IKakaoMapUrl {

    @Value("${KakaoMap.URL.Coordinate}")
    private String kakaoMapCoordinateUrl;

    @Override
    public String getUrl() {
        return kakaoMapCoordinateUrl;
    }
}
