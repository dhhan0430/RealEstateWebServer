package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dh.realestate.model.dto.RealEstateInfo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "real_estate")
public class RealEstateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "real_estate_id")
    private Long id;                        // id
    private String name;                    // name
    private String address;                 // road address
    private String type;                    // apartment or villa
    private double areaForExclusiveUse;     // size of area
    private String marketPrice;             // current market price
    private int buildYear;                  // year of construct ion
    @OneToMany
    @JoinColumn(name = "real_estate_id") // 이렇게 해야 mapping table이 안 생긴다.
    private List<RealEstateAndSubway> realEstateAndSubways = new ArrayList<>(); // list of subway line nearby
    @OneToMany
    @JoinColumn(name = "real_estate_id") // 이렇게 해야 mapping table이 안 생긴다.
    private List<RealEstateAndSupermarket> realEstateAndSupermarkets = new ArrayList<>(); // list of supermarket nearby

    public void addRealEstateAndSubways(RealEstateAndSubway... realEstateAndSubways) {
        Collections.addAll(this.realEstateAndSubways, realEstateAndSubways);
    }

    public void addRealEstateAndSupermarkets(RealEstateAndSupermarket... realEstateAndSupermarkets) {
        Collections.addAll(this.realEstateAndSupermarkets, realEstateAndSupermarkets);
    }

    public RealEstateInfo toDto() {
        var dto = RealEstateInfo.builder()
                .id(getId())
                .name(getName())
                .address(getAddress())
                .type(getType())
                .areaForExclusiveUse(getAreaForExclusiveUse())
                .marketPrice(getMarketPrice())
                .buildYear(getBuildYear())
                .subways(getRealEstateAndSubways()
                        .stream()
                        .map(rssw -> rssw.getSubwayEntity().toDto())
                        .collect(Collectors.toList()))
                .supermarkets(getRealEstateAndSupermarkets()
                        .stream()
                        .map(rsmt -> rsmt.getSupermarketEntity().toDto())
                        .collect(Collectors.toList()))
                .build();

        return dto;
    }

}
