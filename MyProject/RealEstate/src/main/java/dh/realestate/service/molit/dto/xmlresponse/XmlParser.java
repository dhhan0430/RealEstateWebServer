package dh.realestate.service.molit.dto.xmlresponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dh.realestate.service.molit.dto.MolitRealEstateRes;

public class XmlParser {
    public static MolitRealEstateRes parse(String xml) {
        ObjectMapper xmlMapper = new XmlMapper();

        MolitRealEstateRes response = null;

        try {
            response = xmlMapper.readValue(xml, MolitRealEstateRes.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
