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
@Table(name = "supermarket")
public class SupermarketEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeName;
    private String addressName;
    private String placeUrl;
    //private String distance;

    @OneToMany
    @JoinColumn(name = "supermarket_id")
    private List<RealEstateAndSupermarket> realEstateAndSupermarkets = new ArrayList<>();

    public void addRealEstateAndSupermarkets(RealEstateAndSupermarket... realEstateAndSupermarkets) {
        Collections.addAll(this.realEstateAndSupermarkets, realEstateAndSupermarkets);
    }

    public void deleteRealEstateAndSupermarket(RealEstateAndSupermarket realEstateAndSupermarket) {
        realEstateAndSupermarkets.remove(realEstateAndSupermarket);
    }

    public RealEstateInfo.Supermarket toDto() {
        var dto = RealEstateInfo.Supermarket.builder()
                .placeName(getPlaceName())
                .addressName(getAddressName())
                .placeUrl(getPlaceUrl())
                .build();

        return dto;
    }

    public void updateEntity(RealEstateInfo.Supermarket supermarket) {

        setPlaceName(supermarket.getPlaceName());
        setAddressName(supermarket.getAddressName());
        setPlaceUrl(supermarket.getPlaceUrl());
    }

}
