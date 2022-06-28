package dh.realestate.service.molit.url;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MolitApartUrl implements IMolitUrl {

    @Value("${Molit.URL.Apartment}")
    private String molitApartUrl;

    @Override
    public String getUrl() {
        return molitApartUrl;
    }
}
