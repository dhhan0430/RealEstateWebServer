package dh.realestate.service.kakaomap;

import dh.realestate.ApplicationContextProvider;
import dh.realestate.service.kakaomap.dto.KakaoMapCategoryReq;
import dh.realestate.service.kakaomap.dto.KakaoMapCategoryRes;
import dh.realestate.service.kakaomap.dto.KakaoMapCoordinateReq;
import dh.realestate.service.kakaomap.dto.KakaoMapCoordinateRes;
import dh.realestate.service.kakaomap.url.IKakaoMapUrl;
import dh.realestate.service.kakaomap.url.KakaoMapCategoryUrl;
import dh.realestate.service.kakaomap.url.KakaoMapCoordinateUrl;
import dh.realestate.service.molit.dto.MolitRealEstateReq;
import dh.realestate.service.molit.dto.MolitRealEstateRes;
import dh.realestate.service.molit.dto.xmlresponse.XmlParser;
import dh.realestate.service.molit.url.IMolitUrl;
import dh.realestate.service.molit.url.MolitApartUrl;
import dh.realestate.service.molit.url.MolitVillaUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Component
public class KakaoMapClient {

    @Value("${KakaoMap.Authorization}")
    private String authorization;
    @Value("${KakaoMap.Subway.Radius}")
    private Integer subwayRadius;
    @Value("${KakaoMap.Supermarket.Radius}")
    private Integer supermarketRadius;

    public KakaoMapCoordinateRes convertAddressToCoordinate(String address) {

        IKakaoMapUrl iKakaoMapUrl = ApplicationContextProvider.getContext()
                .getBean(KakaoMapCoordinateUrl.class);

        var kakaoMapCoordinateReq = new KakaoMapCoordinateReq(address);

        var uri = UriComponentsBuilder
                .fromUriString(iKakaoMapUrl.getUrl())
                .queryParams(kakaoMapCoordinateReq.toMultiValueMap())
                .build(false).encode().toUri();

        var headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(headers);
        var responseType = new ParameterizedTypeReference<KakaoMapCoordinateRes>(){};

        var responseEntity = new RestTemplate().exchange(
            uri,
            HttpMethod.GET,
            httpEntity,
            responseType
        );

        return responseEntity.getBody();
    }

    public KakaoMapCategoryRes searchNearby(
            String category_group_code, String x, String y) {

        IKakaoMapUrl iKakaoMapUrl = ApplicationContextProvider.getContext()
                .getBean(KakaoMapCategoryUrl.class);

        Integer radius = null;
        if (category_group_code.equals("SW8")) {
            radius = subwayRadius;
        }
        else if (category_group_code.equals("MT1")) {
            radius = supermarketRadius;
        }

        var kakaoMapCategoryReq = new KakaoMapCategoryReq(
                category_group_code, x, y, radius);

        var uri = UriComponentsBuilder
                .fromUriString(iKakaoMapUrl.getUrl())
                .queryParams(kakaoMapCategoryReq.toMultiValueMap())
                .build(false).encode().toUri();

        var headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(headers);
        var responseType = new ParameterizedTypeReference<KakaoMapCategoryRes>(){};

        var responseEntity = new RestTemplate().exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return responseEntity.getBody();
    }


}
