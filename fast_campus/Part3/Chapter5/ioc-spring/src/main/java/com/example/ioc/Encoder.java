package com.example.ioc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/* @Component */
public class Encoder {

    private IEncoder iEncoder;

    // 이 Encoder 클래스를 Component로 지정하면, 스프링에서는 IEncoder iEncoder에
    // Base64Encoder룰 넣어서 싱글톤 객체를 생성해야할지, UrlEncoder를 넣어서 싱글톤 객체를
    // 생성해야할지 모른다. 스프링 컨테이너에서 관리하는 IEncoder 타입의 Bean이 2개 이상이기 때문.
    // 아래 Qualifier에서 base64Encoder인 이유는 Base64Encoder를 Component로 지정할 때,
    // @Component("???") 아무런 이름을 지정해놓지 않으면 첫글자만 소문자로 변경되어 저장되기 때문.
    public Encoder(/* @Qualifier("base64Encoder") */IEncoder iEncoder) {
        this.iEncoder = iEncoder;
    }

    public void setIEncoder(IEncoder iEncoder) {
        this.iEncoder = iEncoder;
    }

    public String encode(String message) {
        return iEncoder.encode(message);
    }
}
