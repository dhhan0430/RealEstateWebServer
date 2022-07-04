package dh.realestate.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "realestate")
public class RealEstate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "realestate_id")
    private Long id;                        // id
    private String name;                    // name
    private String address;                 // road address
    private String type;                    // apartment or villa
    private double areaForExclusiveUse;     // size of area
    private String marketPrice;             // current market price
    private int buildYear;                  // year of construct ion
    @OneToMany
    private List<Subway> subways = new ArrayList<>(); // list of subway line nearby
    @OneToMany
    private List<Supermarket> supermarkets = new ArrayList<>(); // list of supermarket nearby
    // private String hospital;   // list of hospital nearby

}
