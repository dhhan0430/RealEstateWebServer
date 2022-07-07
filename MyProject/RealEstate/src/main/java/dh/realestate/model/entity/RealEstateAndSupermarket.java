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
public class RealEstateAndSupermarket extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RealEstate realEstate;

    @ManyToOne
    private Supermarket supermarket;

}
