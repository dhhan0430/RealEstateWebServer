package dh.realestate.service.molit.dto.xmlresponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dh.realestate.service.molit.dto.MolitRealEstateRes;
import dh.realestate.service.molit.dto.xmlresponse.body.item.ApartItem;
import dh.realestate.service.molit.dto.xmlresponse.body.item.VillaItem;

public class XmlParser {
    public static MolitRealEstateRes parse(String xml, String type) {
        ObjectMapper xmlMapper = new XmlMapper();

        MolitRealEstateRes response = null;

        try {
            if (type.equals("아파트")) {
                response = xmlMapper.readValue(xml, new TypeReference<MolitRealEstateRes<ApartItem>>() {});
            }
            else if (type.equals("빌라")) {
                response = xmlMapper.readValue(xml, new TypeReference<MolitRealEstateRes<VillaItem>>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
