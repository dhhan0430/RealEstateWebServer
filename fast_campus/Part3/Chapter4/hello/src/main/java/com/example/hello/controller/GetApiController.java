package com.example.hello.controller;

import com.example.hello.dto.UserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/get")
public class GetApiController {

    @GetMapping(path = "/hello") // http://localhost:8080/api/get/hello
    public String hello() {
        return "get Hello";
    }

    // @RequestMapping("hi") => get / post/ put / delete .. 모두 동작하게 됨.
    @RequestMapping(path = "/hi", method = RequestMethod.GET)
    // get http://localhost:8080/api/get/hi
    public String hi() {
        return "hi";
    }

    // http://localhost:8080/api/get/path-variable/{name}
    @GetMapping("/path-variable/{name}")
    // public String pathVariable(@PathVariable String name) {
    public String pathVariable(@PathVariable(name = "name") String pathName) {
        System.out.println("PathVariable: " + pathName);
        return pathName;
    }

    // query parameter 란?
    // https://www.google.com/search?q=intellij&hl=ko&sxsrf=ALiCzsawhhJL
    // 7qnrsUjiQ3qAcNinPmsrHA%3A1652269408549&ei=YKF7Yr6ZIb-Yr7wP-Zi40A8
    // &ved=0ahUKEwi-ya2Zr9f3AhU_zIsBHXkMDvoQ4dUDCA4&uact=5&oq=intellij
    // &gs_lcp=Cgdnd3Mtd2l6EAMyBAgjECcyBAgjECcyBAgjECcyCAgAEIAEELEDMgUI
    // ABCABDIFCAAQgAQyCwgAEIAEELEDEIMBMgUIABCABDIFCAAQgAQyBQgAEIAEOgcI
    // IxDqAhAnOgsILhCABBDHARDRAzoRCC4QgAQQsQMQgwEQxwEQ0QM6DgguEIAEELED
    // EMcBENEDSgQIQRgASgQIRhgAUI0HWOgXYPYZaAFwAXgAgAGIAYgBwQeSAQMwLjiY
    // AQCgAQGwAQrAAQE&sclient=gws-wiz
    // ? 부터가 query parameter 다. 주로 검색할 때 사용.
    // 잘 보면 &(and) 연산자가 있다. & 기준으로 key = value 가 온다.
    // ?key=value&key2=value2

    // http://localhost:8080/api/get/query-param?user=steve&email=steve@gmail.com
    // &age=30

    // Map으로 받으면 모든 key를 받을 수 있다. (무엇이 들어올지 모름)
    @GetMapping(path = "query-param")
    public String queryParam(@RequestParam Map<String,String> queryParam) {

        StringBuilder sb = new StringBuilder();

        queryParam.entrySet().forEach( entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("\n");

            sb.append(entry.getKey() + " = " + entry.getValue() + "\n");

        });

        return sb.toString();
    }

    @GetMapping(path ="query-param02")
    public String queryParam02(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam int age
    ) {
        System.out.println(name);
        System.out.println(email);
        System.out.println(age);

        return name + " " + email + " " + age ;
    }

    // 이 형태(input에 객체를 활용)가 현업에서 가장 많이 쓰임.
    // @RequestParam annotation 사용하지 않음.
    @GetMapping("query-param03")
    public String queryParam03(UserRequest userRequest) {
        System.out.println(userRequest.getName());
        System.out.println(userRequest.getEmail());
        System.out.println(userRequest.getAge());

        return userRequest.toString() ;
    }

}
