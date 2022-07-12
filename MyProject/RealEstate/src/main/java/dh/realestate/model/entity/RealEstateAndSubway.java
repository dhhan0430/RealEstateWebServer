package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.model.entity.listener.BaseEntityListener;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(value = BaseEntityListener.class)
public class RealEstateAndSubway extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String distance;
    @NonNull
    private String realEstate;
    @NonNull
    private String subway;

    public void updateEntity(String realEstateName, RealEstateInfo.Subway subway) {
        setDistance(subway.getDistance());
        setRealEstate(realEstateName);
        setSubway(subway.getPlaceName());
    }

    @NonNull
    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    @ToString.Exclude
    private RealEstateEntity realEstateEntity;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "subway_id")
    @ToString.Exclude
    private SubwayEntity subwayEntity;
}
