package dh.realestate.service.molit.dto.xmlresponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlParser {
    public static Response parse(String xml) {
        ObjectMapper xmlMapper = new XmlMapper();

        Response response = null;

        try {
            response = xmlMapper.readValue(xml, Response.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
