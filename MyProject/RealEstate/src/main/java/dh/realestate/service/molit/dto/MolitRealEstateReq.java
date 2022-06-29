package dh.realestate.service.molit.dto;

import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MolitRealEstateReq {

    @NonNull
    private String serviceKey;
    @NonNull
    private String regionCode;
    @NonNull
    private String dealDate;

    public MultiValueMap<String, String> toMultiValueMap() {

        var map = new LinkedMultiValueMap<String, String>();

        map.add("ServiceKey", serviceKey);
        map.add("LAWD_CD", regionCode);
        map.add("DEAL_YMD", dealDate);

        return map;
    }

}
