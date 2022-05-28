package com.example.interceptor.interceptor;

import com.example.interceptor.annotation.Auth;
import com.example.interceptor.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    // Interceptor
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String url = request.getRequestURI();

        URI uri = UriComponentsBuilder.fromUriString(request.getRequestURI())
                .query(request.getQueryString()).build().toUri();

        log.info("request url : {}", url);

        // 이런 checkAnnotation() 같이 권한을 확인하는 것은,
        // Interceptor 가 Spring context 에서 관리되고 있기 때문에 가능하다.
        // Filter 는 Web Application 에서 관리하기 때문에,
        // Object handler 가 없어서 이런 작업을 해주지 못한다.
        boolean hasAnnotation = checkAnnotation(handler, Auth.class);
        log.info("has annotation : {}", hasAnnotation);

        // 나의 서버는 모두 public 으로 동작을 하는데,
        // 단! Auth 권한을 가진 요청에 대해서는, 세션, 쿠키 등을 체크하겠다.
        if (hasAnnotation) {
            // 권한 체크
            String query = uri.getQuery();
            log.info("query : {}", query);
            if (query.equals("name=steve")) {
                return true;
            }

            // return false;

            throw new AuthException();
        }

        // 여기서 false를 리턴하면, Interceptor 아키텍처에서 보이듯
        // HandlerInterceptor -> Handler 로 넘어가지 않는다.
        return true;
    }

    private boolean checkAnnotation(Object handler, Class clazz) {

        // resource javascript, html, ... 에 대한 요청일 때는 통과를 시켜줘야 한다.
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // annotation check
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (handlerMethod.getMethodAnnotation(clazz) != null ||
                handlerMethod.getBeanType().getAnnotation(clazz) != null) {
            //Auth annotation 이 있을 때는 true
            return true;
        }

        return false;
    }
}
