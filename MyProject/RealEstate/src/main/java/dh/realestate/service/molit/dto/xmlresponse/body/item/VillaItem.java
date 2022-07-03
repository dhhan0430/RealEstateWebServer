package dh.realestate.service.molit.dto.xmlresponse.body.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class VillaItem extends Item {

    @JsonProperty("대지권면적")
    private double landArea;
    @JsonProperty("연립다세대")
    private String apartName;
}
