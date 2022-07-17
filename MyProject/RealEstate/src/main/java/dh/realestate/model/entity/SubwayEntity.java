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
@Table(name = "subway")
public class SubwayEntity extends BaseEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeName;
    private String addressName;
    private String placeUrl;
    //private String distance;

    @OneToMany
    @JoinColumn(name = "subway_id")
    private List<RealEstateAndSubway> realEstateAndSubways = new ArrayList<>();

    public void addRealEstateAndSubways(RealEstateAndSubway... realEstateAndSubways) {
        Collections.addAll(this.realEstateAndSubways, realEstateAndSubways);
    }

    public void deleteRealEstateAndSubway(RealEstateAndSubway realEstateAndSubway) {
        realEstateAndSubways.remove(realEstateAndSubway);
    }

    public RealEstateInfo.Subway toDto() {
        var dto = RealEstateInfo.Subway.builder()
                .placeName(getPlaceName())
                .addressName(getAddressName())
                .placeUrl(getPlaceUrl())
                .build();

        return dto;
    }

    public SubwayEntity updateEntity(RealEstateInfo.Subway subway) {

        setPlaceName(subway.getPlaceName());
        setAddressName(subway.getAddressName());
        setPlaceUrl(subway.getPlaceUrl());

        return this;
    }
}
