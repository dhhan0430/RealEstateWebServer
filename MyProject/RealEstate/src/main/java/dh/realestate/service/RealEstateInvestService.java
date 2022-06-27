package dh.realestate.service;

import dh.realestate.model.dto.RealEstateDto;
import dh.realestate.service.kakaomap.KakaoMapClient;
import dh.realestate.service.molit.MolitClient;
import dh.realestate.service.molit.MolitCode;
import dh.realestate.service.molit.dto.MolitRealEstateReq;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RealEstateInvestService {

    private final MolitClient molitClient;
    private final KakaoMapClient kakaoMapClient;


    public RealEstateDto search(String region,
                                String type,
                                Integer lowPrice,
                                Integer highPrice,
                                Integer buildingYear)
            throws FileNotFoundException, UnsupportedEncodingException {

        // 예외 처리

        // input parameter: 1.region, 2.type
        List = MolitClient.searchRealEstate(region, type);





    }

}
