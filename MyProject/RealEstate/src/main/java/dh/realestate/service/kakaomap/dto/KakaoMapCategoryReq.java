package dh.realestate.service.kakaomap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMapCategoryReq {

    private String category_group_code;
    private String x;
    private String y;
    private int radius;

    public MultiValueMap<String, String> toMultiValueMap() {

        var map = new LinkedMultiValueMap<String, String>();

        map.add("category_group_code", category_group_code);
        map.add("x", x);
        map.add("y", y);
        map.add("radius", String.valueOf(radius));

        return map;
    }

}
