package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.dto.Car;
import org.example.dto.User;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {

        System.out.println("Main!");

        ObjectMapper objectMapper = new ObjectMapper();

        User user = new User();
        user.setName("홍길동");
        user.setAge(10);

        Car car1 = new Car();
        car1.setName("K5");
        car1.setCarNumber("11가 1111");
        car1.setType("sedan");

        Car car2 = new Car();
        car2.setName("Q5");
        car2.setCarNumber("22가 2222");
        car2.setType("suv");

        List<Car> carList = Arrays.asList(car1, car2);
        user.setCars(carList);

        //System.out.println(user);

        // Object 객체 -> json
        String json = objectMapper.writeValueAsString(user);
        System.out.println(json);

        // json node에 접근하여 각 멤버에 접근
        JsonNode jsonNode = objectMapper.readTree(json);
        String _name = jsonNode.get("name").asText();
        int _age = jsonNode.get("age").asInt();
        System.out.println("name: " + _name);
        System.out.println("age: " + _age);

        // String _List = jsonNode.get("cars").asText();
        //System.out.println(_List);

        JsonNode cars = jsonNode.get("cars");
        // JsonNode(parent) -> ArrayNode(child)
        ArrayNode arrayNode = (ArrayNode)cars;
        // convertValue() : json이 아닌 우리가 원하는 클래스로 매핑해줌.
        List<Car> _cars = objectMapper.convertValue(arrayNode,
                new TypeReference<List<Car>>() {});
        System.out.println(_cars);


        // JsonNode 객체의 값을 바꿔줄 수도 있다.
        // JsonNode(parent) -> ObjectNode(child)
        ObjectNode objectNode = (ObjectNode)jsonNode;
        objectNode.put("name", "steve");
        objectNode.put("age", 20);

        System.out.println(objectNode.toPrettyString()); // json을 예쁘게 출력.

    }
}