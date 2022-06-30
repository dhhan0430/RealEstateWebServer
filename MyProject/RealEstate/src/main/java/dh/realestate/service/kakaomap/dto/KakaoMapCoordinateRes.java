package dh.realestate.service.kakaomap.dto;

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
        private String address_name;
        private String address_type;
        private String x;
        private String y;
        private Address address;
        private RoadAddress roadAddress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String region_3depth_h_name;
        private String h_code;
        private String b_code;
        private String mountain_yn;
        private String main_address_no;
        private String sub_address_no;
        private String x;
        private String y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoadAddress {
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String road_name;
        private String underground_yn;
        private String main_building_no;
        private String sub_building_no;
        private String building_name;
        private String zone_no;
        private String x;
        private String y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoMapCoordinateMeta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }
}
