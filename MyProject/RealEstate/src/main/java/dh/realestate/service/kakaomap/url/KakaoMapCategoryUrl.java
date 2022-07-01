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
public class KakaoMapCategoryUrl implements IKakaoMapUrl {

    @Value("${KakaoMap.URL.Category}")
    private String kakaoMapCategoryUrl;

    @Override
    public String getUrl() {
        return kakaoMapCategoryUrl;
    }
}
