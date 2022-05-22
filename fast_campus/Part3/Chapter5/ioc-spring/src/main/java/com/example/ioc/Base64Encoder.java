package com.example.ioc;

import org.springframework.stereotype.Component;

import java.util.Base64;

// Component로 지정해 놓으면, 스프링이 실행이 될 때, 저 Annotation을 찾아서 직접 객체를
// 싱글톤 형태로 만들어서 Spring Container에서 관리해준다.
@Component
public class Base64Encoder implements IEncoder {

    public String encode(String message) {

        return Base64.getEncoder().encodeToString(message.getBytes());
    }
}
