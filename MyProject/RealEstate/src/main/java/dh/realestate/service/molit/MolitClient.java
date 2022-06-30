package dh.realestate.service.molit;

import dh.realestate.ApplicationContextProvider;
import dh.realestate.service.molit.dto.MolitRealEstateReq;
import dh.realestate.service.molit.dto.MolitRealEstateRes;
import dh.realestate.service.molit.dto.xmlresponse.XmlParser;
import dh.realestate.service.molit.url.IMolitUrl;
import dh.realestate.service.molit.url.MolitApartUrl;
import dh.realestate.service.molit.url.MolitVillaUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class MolitClient {

    @Value("${Molit.ServiceKey}")
    private String serviceKey;
    @Value("${Molit.DealDate}")
    private String dealDate;

    public MolitRealEstateRes searchRealEstate(String region, String type)
            throws FileNotFoundException, UnsupportedEncodingException {

        IMolitUrl iMolitUrl = null;
        if (type.equals("아파트")) {
            iMolitUrl = ApplicationContextProvider.getContext()
                    .getBean(MolitApartUrl.class);
        }
        else if (type.equals("빌라")) {
            iMolitUrl = ApplicationContextProvider.getContext()
                    .getBean(MolitVillaUrl.class);
        }
        else {
            // throw
        }

        // Region Code 찾기
        var regionCode = MolitCode.codeSearch(region);
        if (regionCode == null) {
            // throw
        }

        System.out.println(iMolitUrl.getUrl());

        var molitRealEstateReq = new MolitRealEstateReq(serviceKey, regionCode, dealDate);

        var uri = UriComponentsBuilder
                .fromUriString(iMolitUrl.getUrl())
                .queryParams(molitRealEstateReq.toMultiValueMap())
                .build(true).toUri();

        var headers = new HttpHeaders();
        var httpEntity = new HttpEntity<>(headers);
        var responseType = new ParameterizedTypeReference<String>(){};

        var restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        var responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return XmlParser.parse(responseEntity.getBody());

    }



}