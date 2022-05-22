package com.example.ioc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication // Bean: 스프링이 관리하는 객체들
public class IocSpringApplication {

    public static void main(String[] args) {

        SpringApplication.run(IocSpringApplication.class, args);

        ApplicationContext context = ApplicationContextProvider.getContext();

        String url = "www.naver.com/books/it?page=10&size=20&name=spring-boot";

        /* option 1
        // IoC(객체 관리)는 스프링 컨테이너가 해준다.
        Base64Encoder base64Encoder = context.getBean(Base64Encoder.class);
        // DI(객체 주입)는 아래와 같이 개발자가 해준다.
        Encoder encoder = new Encoder(base64Encoder);

        String result = encoder.encode(url);
        System.out.println(result);

        UrlEncoder urlEncoder = context.getBean(UrlEncoder.class);
        encoder.setIEncoder(urlEncoder);
        result = encoder.encode(url);
        System.out.println(result);
        */

        /* option 2. Encoder도 Bean으로 등록하여 관리.
        Encoder encoder = context.getBean(Encoder.class);
        String result = encoder.encode(url);
        System.out.println(result);
        */

        // option 3. @Configuration 사용하여 여러 개의 Encoder를 Bean으로 등록.
        Encoder encoder = context.getBean("urlEncoder2", Encoder.class);
        String result = encoder.encode(url);
        System.out.println(result);

    }

}

// @Configuration: 1개의 클래스에서 여러 개의 Bean을 등록할 때 사용.
// 지금 2개의 인코더를 만들어서 관리하려고 한다. Encoder(Base64Encoder), Encoder(UrlEncoder)
@Configuration
class AppConfig {

    @Bean("base64Encoder2")
    public Encoder encoder(Base64Encoder base64Encoder) {
        return new Encoder(base64Encoder);
    }

    @Bean("urlEncoder2")
    public Encoder encoder(UrlEncoder urlEncoder) {
        return new Encoder(urlEncoder);
    }
}