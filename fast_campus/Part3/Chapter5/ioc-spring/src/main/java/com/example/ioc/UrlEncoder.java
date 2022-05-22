package com.example.ioc;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// Component로 지정해 놓으면, 스프링이 실행이 될 때, 저 Annotation을 찾아서 직접 객체를
// 싱글톤 형태로 만들어서 Spring Container에서 관리해준다.
// Spring Container에 접근해서 객체를 가져오기 위해선 코드가 필요하다.
@Component
public class UrlEncoder implements IEncoder{

    public String encode(String message) {
        try {
            return URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
