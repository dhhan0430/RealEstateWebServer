package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dh.realestate.model.dto.RealEstateInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                        // id
    private String name;                    // name
    private String address;                 // road address
    private String type;                    // apartment or villa
    private double areaForExclusiveUse;     // size of area
    private String marketPrice;             // current market price
    private int buildYear;                  // year of construct ion
    @ElementCollection
    private List<Subway> subways = new ArrayList<>(); // list of subway line nearby
    @ElementCollection
    private List<Supermarket> supermarkets = new ArrayList<>(); // list of supermarket nearby
    // private String hospital;   // list of hospital nearby

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    @Embeddable
    public static class Subway {

        private String placeName;
        private String addressName;
        private String placeUrl;
        private String distance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    @Embeddable
    public static class Supermarket {

        private String placeName;
        private String addressName;
        private String placeUrl;
        private String distance;
    }

}
