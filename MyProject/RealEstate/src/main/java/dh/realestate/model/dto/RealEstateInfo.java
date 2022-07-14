package dh.realestate.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dh.realestate.model.entity.RealEstateEntity;
import dh.realestate.model.entity.SubwayEntity;
import dh.realestate.model.entity.SupermarketEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RealEstateInfo implements Comparable<RealEstateInfo> {

    private Long id;
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

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Subway {

        private String placeName;
        private String addressName;
        private String placeUrl;
        private String distance;    // RealEstateAndSubway 에서 get

        public SubwayEntity toEntity() {
            var entity = SubwayEntity.builder()
                    .placeName(getPlaceName())
                    .addressName(getAddressName())
                    .placeUrl(getPlaceUrl())
                    .realEstateAndSubways(new ArrayList<>())
                    .build();

            return entity;
        }

        public boolean equalsTo(SubwayEntity subwayEntity) {
            if (getPlaceName().equals(subwayEntity.getPlaceName()) &&
                    getAddressName().equals(subwayEntity.getAddressName())) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Supermarket {

        private String placeName;
        private String addressName;
        private String placeUrl;
        private String distance;    // RealEstateAndSupermarket 에서 get

        public SupermarketEntity toEntity() {
            var entity = SupermarketEntity.builder()
                    .placeName(getPlaceName())
                    .addressName(getAddressName())
                    .placeUrl(getPlaceUrl())
                    .realEstateAndSupermarkets(new ArrayList<>())
                    .build();

            return entity;
        }

        public boolean equalsTo(SupermarketEntity supermarketEntity) {
            if (getPlaceName().equals(supermarketEntity.getPlaceName()) &&
                    getAddressName().equals(supermarketEntity.getAddressName())) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public RealEstateEntity toEntity() {
        var entity = RealEstateEntity.builder()
                .name(getName())
                .address(getAddress())
                .type(getType())
                .areaForExclusiveUse(getAreaForExclusiveUse())
                .marketPrice(getMarketPrice())
                .buildYear(getBuildYear())
                .realEstateAndSubways(new ArrayList<>())
                .realEstateAndSupermarkets(new ArrayList<>())
                .build();

        return entity;
    }

    public String combineNameAndAddress() {
        return new StringBuilder()
                .append(getName()).append(":").append(getAddress()).toString();
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
