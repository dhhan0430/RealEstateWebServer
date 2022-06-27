package dh.realestate.service.molit;

import dh.realestate.service.molit.dto.MolitRealEstateReq;
import dh.realestate.service.molit.url.IMolitUrl;
import dh.realestate.service.molit.url.MolitApartUrl;
import dh.realestate.service.molit.url.MolitVillaUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@Component
public class MolitClient {

    public ArrayList<String> searchRealEstate(String region, String type)
            throws FileNotFoundException, UnsupportedEncodingException {

        IMolitUrl iMolitUrl = null;
        if (type.equals("아파트")) {
            iMolitUrl = new MolitApartUrl();
        }
        else if (type.equals("빌라")) {
            iMolitUrl = new MolitVillaUrl();
        }
        else {
            // throw
        }

        // Region Code 찾기
        var regionCode = MolitCode.codeSearch(region);
        if (regionCode == null) {
            // throw
        }

        var molitRealEstateReq = new MolitRealEstateReq(regionCode);

        var uri = UriComponentsBuilder
                .fromUriString(iMolitUrl.getUrl())
                .queryParams(molitRealEstateReq.toMultiValueMap())
                .build().encode().toUri();

    }



}
