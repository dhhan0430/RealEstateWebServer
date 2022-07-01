package dh.realestate.service.kakaomap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMapCoordinateRes {

    private List<KakaoMapCoordinateDoc> documents;
    private KakaoMapCoordinateMeta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoMapCoordinateDoc {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("address_type")
        private String addressType;
        private String x;
        private String y;
        private Address address;
        @JsonProperty("road_address")
        private RoadAddress roadAddress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("region_1depth_name")
        private String region1depthName;
        @JsonProperty("region_2depth_name")
        private String region2depthName;
        @JsonProperty("region_3depth_name")
        private String region3depthName;
        @JsonProperty("region_3depth_h_name")
        private String region3depthHName;
        @JsonProperty("h_code")
        private String hCode;
        @JsonProperty("b_code")
        private String bCode;
        @JsonProperty("mountain_yn")
        private String mountainYn;
        @JsonProperty("main_address_no")
        private String mainAddressNo;
        @JsonProperty("sub_address_no")
        private String subAddressNo;
        private String x;
        private String y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoadAddress {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("region_1depth_name")
        private String region1depthName;
        @JsonProperty("region_2depth_name")
        private String region2depthName;
        @JsonProperty("region_3depth_name")
        private String region3depthName;
        @JsonProperty("road_name")
        private String roadName;
        @JsonProperty("underground_yn")
        private String undergroundYn;
        @JsonProperty("main_building_no")
        private String mainBuildingNo;
        @JsonProperty("sub_building_no")
        private String subBuildingNo;
        @JsonProperty("building_name")
        private String buildingName;
        @JsonProperty("zone_no")
        private String zoneNo;
        private String x;
        private String y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoMapCoordinateMeta {
        @JsonProperty("total_count")
        private int totalCount;
        @JsonProperty("pageable_count")
        private int pageableCount;
        @JsonProperty("is_end")
        private boolean isEnd;
    }
}
