package dh.realestate.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RealEstateList {

    @NonNull
    private String region;
    @NonNull
    private String type;
    @NonNull
    private String dealDate;
    @NonNull
    private String marketPriceRange;
    @NonNull
    private String buildYearRange;
    @NonNull
    private int totalCount;
    private List<RealEstateInfo> realEstateList = new ArrayList<>();
}
