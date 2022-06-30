package dh.realestate.service.kakaomap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMapCoordinateReq {

    @NonNull
    private String query;

    public MultiValueMap<String, String> toMultiValueMap() {

        var map = new LinkedMultiValueMap<String, String>();

        map.add("query", query);

        return map;
    }
}
