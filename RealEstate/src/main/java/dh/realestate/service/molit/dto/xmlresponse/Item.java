package dh.realestate.service.molit.dto.xmlresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int dealAmount;
    private int buildYear;
    private int dealYear;
    private String dong;
    private String apartmentName;
    private int dealMonth;
    private int dealDay;
    private double areaForExclusiveUse;
    private String jibun;
    private int regionalCode;
    private int floor;
    private String cancelDealType;
    private String cancelDealDay;

}
