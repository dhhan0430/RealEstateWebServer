package dh.realestate.service.molit.dto;

import dh.realestate.service.molit.dto.xmlresponse.Body;
import dh.realestate.service.molit.dto.xmlresponse.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MolitRealEstateRes {
    private Header header;
    private Body body;
}
