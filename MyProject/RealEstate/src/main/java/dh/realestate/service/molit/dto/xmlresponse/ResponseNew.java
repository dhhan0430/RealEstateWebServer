package dh.realestate.service.molit.dto.xmlresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseNew {
    private Header header;
    private Body body;
}