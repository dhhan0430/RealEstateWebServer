package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dh.realestate.model.dto.RealEstateInfo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SupermarketEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeName;
    private String addressName;
    private String placeUrl;
    private String distance;

    @OneToMany
    @JoinColumn(name = "supermarket")
    private List<RealEstateAndSupermarket> realEstateAndSupermarkets = new ArrayList<>();

    public void addRealEstateAndSupermarkets(RealEstateAndSupermarket... realEstateAndSupermarkets) {
        Collections.addAll(this.realEstateAndSupermarkets, realEstateAndSupermarkets);
    }

    public RealEstateInfo.Supermarket toDto() {
        var dto = RealEstateInfo.Supermarket.builder()
                .placeName(getPlaceName())
                .addressName(getAddressName())
                .placeUrl(getPlaceUrl())
                .distance(getDistance())
                .build();

        return dto;
    }
}
