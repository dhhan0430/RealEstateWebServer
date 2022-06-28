package dh.realestate.service.molit.dto.xmlresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Body {
    private List<Item> items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
