package dh.realestate.service.molit.dto.xmlresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @JsonProperty("거래금액")
    private String dealAmount;
    @JsonProperty("거래유형")
    private String dealType;
    @JsonProperty("건축년도")
    private int buildYear;
    @JsonProperty("년")
    private int dealYear;
    @JsonProperty("법정동")
    private String townName;
    @JsonProperty("아파트")
    private String apartName;
    @JsonProperty("월")
    private int dealMonth;
    @JsonProperty("일")
    private int dealDay;
    @JsonProperty("전용면적")
    private double areaForExclusiveUse;
    @JsonProperty("중개사소재지")
    private String brokerLocation;
    @JsonProperty("지번")
    private String lotNumber;
    @JsonProperty("지역코드")
    private int regionalCode;
    @JsonProperty("층")
    private int floor;
    @JsonProperty("해제사유발생일")
    private String cancelDealType;
    @JsonProperty("해제여부")
    private String cancelDealDay;

}
