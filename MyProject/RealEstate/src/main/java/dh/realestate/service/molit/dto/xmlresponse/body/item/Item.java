package dh.realestate.service.molit.dto.xmlresponse.body.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Item {

    @JsonProperty("거래금액")
    protected String dealAmount;
    @JsonProperty("거래유형")
    protected String dealType;
    @JsonProperty("건축년도")
    protected int buildYear;
    @JsonProperty("년")
    protected int dealYear;
    @JsonProperty("법정동")
    protected String townName;
    @JsonProperty("월")
    protected int dealMonth;
    @JsonProperty("일")
    protected int dealDay;
    @JsonProperty("전용면적")
    protected double areaForExclusiveUse;
    @JsonProperty("중개사소재지")
    protected String brokerLocation;
    @JsonProperty("지번")
    protected String lotNumber;
    @JsonProperty("지역코드")
    protected int regionalCode;
    @JsonProperty("층")
    protected int floor;
    @JsonProperty("해제사유발생일")
    protected String cancelDealType;
    @JsonProperty("해제여부")
    protected String cancelDealDay;

    public abstract String getApartName();
}
