package dh.realestate.service.molit.dto;

import dh.realestate.service.molit.dto.xmlresponse.body.Body;
import dh.realestate.service.molit.dto.xmlresponse.body.item.Item;
import dh.realestate.service.molit.dto.xmlresponse.header.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MolitRealEstateRes<T extends Item> {

    private Header header;
    private Body<T> body;
}
