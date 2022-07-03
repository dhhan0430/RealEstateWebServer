package dh.realestate.service.molit.dto.xmlresponse.body;

import dh.realestate.service.molit.dto.xmlresponse.body.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Body<T extends Item> {

    private List<T> items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
