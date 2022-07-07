package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class Subway extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeName;
    private String addressName;
    private String placeUrl;
    private String distance;

    @OneToMany
    @JoinColumn(name = "subway")
    private List<RealEstateAndSubway> realEstateAndSubways = new ArrayList<>();

    public void addRealEstateAndSubways(RealEstateAndSubway... realEstateAndSubways) {
        Collections.addAll(this.realEstateAndSubways, realEstateAndSubways);
    }
}
