package dh.realestate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateInfoDto {

    private Integer index;          // id
    private String name;            // name
    private String type;            // apartment or villa
    private String address;         // road address
    private String marketPrice;     // current market price
    private String buildingYear;    // year of construction
    private String subway;          // list of subway line nearby
    private String hospital;        // list of hospital nearby
    private String supermarket;     // list of supermarket nearby

}
