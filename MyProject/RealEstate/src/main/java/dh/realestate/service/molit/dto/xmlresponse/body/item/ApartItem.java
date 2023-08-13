package dh.realestate.service.molit.dto.xmlresponse.body.item;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceConstructor;

import java.lang.annotation.ElementType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ApartItem extends Item {

    @JsonProperty("아파트")
    private String apartName;
    @JsonProperty("등기일자")
    protected String registrationDate;
}
