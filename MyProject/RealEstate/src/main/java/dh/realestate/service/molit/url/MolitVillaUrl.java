package dh.realestate.service.molit.url;

import org.springframework.beans.factory.annotation.Value;

public class MolitVillaUrl implements IMolitUrl {

    @Value("${Molit.URL.Villa}")
    private String molitVillaUrl;

    @Override
    public String getUrl() {
        return molitVillaUrl;
    }
}
