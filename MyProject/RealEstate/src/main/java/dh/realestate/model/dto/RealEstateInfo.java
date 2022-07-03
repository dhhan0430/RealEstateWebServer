package dh.realestate.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RealEstateInfo implements Comparable<RealEstateInfo> {

    private Integer id;
    @NonNull
    private String name;                    // name
    @NonNull
    private String address;                 // road address
    @NonNull
    private String type;                    // apartment or villa
    @NonNull
    private double areaForExclusiveUse;     // size of area
    @NonNull
    private String marketPrice;             // current market price
    @NonNull
    private int buildYear;                  // year of construct ion
    private List<Subway> subways = new ArrayList<>(); // list of subway line nearby
    private List<Supermarket> supermarkets = new ArrayList<>(); // list of supermarket nearby
    // private String hospital;   // list of hospital nearby

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
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
    public static class Supermarket {

        private String placeName;
        private String addressName;
        private String placeUrl;
        private String distance;
    }

    @Override
    public int compareTo(RealEstateInfo realEstateInfo) {

        var thisValue = Integer.parseInt(this.marketPrice);
        var comparedValue = Integer.parseInt(realEstateInfo.marketPrice);
        if (thisValue > comparedValue) {
            return 1;
        }
        else if (thisValue == comparedValue) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
