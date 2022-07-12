package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dh.realestate.model.dto.RealEstateInfo;
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
public class RealEstateAndSupermarket extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String distance;
    @NonNull
    private String realEstate;
    @NonNull
    private String supermarket;

    public void updateEntity(String supermarketName, RealEstateInfo.Supermarket supermarket) {
        setDistance(supermarket.getDistance());
        setRealEstate(supermarketName);
        setSupermarket(supermarket.getPlaceName());
    }

    @NonNull
    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    @ToString.Exclude
    private RealEstateEntity realEstateEntity;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "supermarket_id")
    @ToString.Exclude
    private SupermarketEntity supermarketEntity;

}
