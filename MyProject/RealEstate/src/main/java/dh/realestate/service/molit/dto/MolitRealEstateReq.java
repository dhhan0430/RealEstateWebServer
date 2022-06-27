package dh.realestate.service.molit.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class MolitRealEstateReq {

    @Value("${Molit.ServiceKey}")
    private String serviceKey;
    @NonNull
    private String regionCode;
    @Value("${Molit.DealDate}")
    private String dealDate;

    public MultiValueMap<String, String> toMultiValueMap() {

        var map = new LinkedMultiValueMap<String, String>();

        map.add("ServiceKey", serviceKey);
        map.add("LAWD_CD", regionCode);
        map.add("DEAL_YMD", dealDate);

        return map;
    }

}
