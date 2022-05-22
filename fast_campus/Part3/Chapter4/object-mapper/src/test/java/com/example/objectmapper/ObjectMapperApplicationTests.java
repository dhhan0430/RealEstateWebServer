package com.example.objectmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ObjectMapperApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        System.out.println("----------");

        // Object-Mapper 란?
        // Text 형태의 JSON -> Object
        // Object -> Text 형태의 JSON

        // RestController
        // req json(text) -> object
        // res object -> json(text)

        // 위처럼 controller를 활용하지 않고, 직접 객체로 생성을 해서 object-mapper를 활용 가능.
        var objectMapper = new ObjectMapper();

        // object -> text
        var user = new User("steve", 10, "010-1111-2222");
        // ObjectMapper는 Get Method를 참조하기 때문에, User 클래스에 get Method 추가해야 된다.
        // 여기서 주의할 점은 User 클래스에 get~으로 시작하는 메서드를 생성해서는 안된다는 것이다.
        // ObjectMapper가 해당 get~ 메서드를 참조하려고 하려고 할 것이기 때문에 에러가 발생한다.
        var text = objectMapper.writeValueAsString(user);
        System.out.println(text);

        // text -> object
        // ObjectMapper는 변환하려는 클래스의 default 생성자가 필요하기 때문에,
        // User 클래스에 default 생성자를 추가해야 된다.
        var objectUser = objectMapper.readValue(text, User.class);
        System.out.println(objectUser);

        System.out.println("----------");
    }

}
