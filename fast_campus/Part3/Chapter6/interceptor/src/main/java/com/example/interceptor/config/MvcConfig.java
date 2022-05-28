package com.example.interceptor.config;

import com.example.interceptor.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// class 안에 final로 생성된 객체들을 생성자에서 주입받을 수 있도록 해준다.
// AuthInterceptor 를 매개변수로 받는 MvcConfig() 생성자가 생긴다.
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    // AuthInterceptor 클래스
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        // 아래와 같이 특정 주소에 대해서만 interceptor 가 동작하게 할 수 있다.
        //registry.addInterceptor(authInterceptor).addPathPatterns("/api/private/*");
        // 여러 Interceptor 들을 등록하여 인증 layer를 구현할 수 있다.
        // 등록된 순서대로 Interceptor가 동작한다.
        // registry.addInterceptor(???)
    }
}
