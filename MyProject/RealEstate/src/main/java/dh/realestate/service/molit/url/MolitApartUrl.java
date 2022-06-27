package dh.realestate.service.molit.url;

import org.springframework.beans.factory.annotation.Value;

public class MolitApartUrl implements IMolitUrl {

    @Value("${Molit.URL.Apartment}")
    private String molitApartUrl;

    @Override
    public String getUrl() {
        return molitApartUrl;
    }
}
