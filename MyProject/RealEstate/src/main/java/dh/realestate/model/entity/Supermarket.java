package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Supermarket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeName;
    private String addressName;
    private String placeUrl;
    private String distance;

    // ManyToOne 을 가진 Entity는 기본적으로 테이블에 One(RealEstate)의 id(real_estate_id)를 가지고 있다.
    @ManyToOne
    //@JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;
}
