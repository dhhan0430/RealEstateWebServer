package dh.realestate.service.kakaomap.dto;

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
        private String place_name;
        private String category_name;
        private String category_group_code;
        private String category_group_name;
        private String phone;
        private String address_name;
        private String road_address_name;
        private String x;
        private String y;
        private String place_url;
        private String distance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoMapCategoryMeta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
        private RegionInfo same_name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionInfo {
        private String[] region;
        private String keyword;
        private String selected_region;
    }
}
