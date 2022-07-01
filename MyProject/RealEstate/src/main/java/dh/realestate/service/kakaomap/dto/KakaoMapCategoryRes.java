package dh.realestate.service.kakaomap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMapCategoryRes {

    private List<KakaoMapCategoryDoc> documents;
    private KakaoMapCategoryMeta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoMapCategoryDoc {
        private String id;
        @JsonProperty("place_name")
        private String placeName;
        @JsonProperty("category_name")
        private String categoryName;
        @JsonProperty("category_group_code")
        private String categoryGroupCode;
        @JsonProperty("category_group_name")
        private String categoryGroupName;
        private String phone;
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("road_address_name")
        private String roadAddressName;
        private String x;
        private String y;
        @JsonProperty("place_url")
        private String placeUrl;
        private String distance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoMapCategoryMeta {
        @JsonProperty("total_count")
        private int totalCount;
        @JsonProperty("pageable_count")
        private int pageableCount;
        @JsonProperty("is_end")
        private boolean isEnd;
        @JsonProperty("same_name")
        private RegionInfo sameName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionInfo {
        private String[] region;
        private String keyword;
        @JsonProperty("selected_region")
        private String selectedRegion;
    }
}
