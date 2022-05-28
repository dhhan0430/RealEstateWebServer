package com.example.filter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
// @Component // 스프링에 의해서 bean으로 관리.
// 주소 설정하여 원하는 controller 에 filter 적용 가능
@WebFilter(urlPatterns = "/api/temp/*")
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // 전처리

        //HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        // 캐싱을 할 수 있다. 하지만 여기선 Byte 크기만 저장해 놓는다. 실제 내용은 아직 저장 안 함.
        ContentCachingRequestWrapper httpServletRequest
                = new ContentCachingRequestWrapper((HttpServletRequest)request);

        //HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        // 캐싱을 할 수 있다. 하지만 여기선 Byte 크기만 저장해 놓는다. 실제 내용은 아직 저장 안 함.
        ContentCachingResponseWrapper httpServletResponse
                = new ContentCachingResponseWrapper((HttpServletResponse)response);

        // 여기 filter 에서 request 를 읽어버리면 커서가 eof에 도달하기 때문에,
        // controller 에서 읽으려고 할 때 에러가 발생한다.
        // doFilter()가 일어난 후에 캐싱이 되기 때문에 아직 읽지 않는다.(주석 처리)
        /*
        BufferedReader br = httpServletRequest.getReader();
        br.lines().forEach(line -> {
            log.info("url : {}, line: {}", url, line);
        });
        */

        // filter 단에서는 request 와 response 내용을 변경시켜줄 수 있다.
        // doFilter()를 통해 실제 내부 스프링 안으로 들어가 캐시에 내용을 담아놓는다.
        // doFilter()에서 controller 의 로직이 전부 실행되는 것으로 보인다.
        chain.doFilter(httpServletRequest, httpServletResponse);

        // 위에서 캐싱을 해놓았기 때문에 계속 읽을 수 있다.

        String url = httpServletRequest.getRequestURI();

        // 후처리
        // request 내용 읽어오기.
        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request url: {}, request body : {}", url, reqContent);

        // controller 처리 끝나고 response 에 담겨서 올 것이다.
        // getContentAsByteArray() 실행하면, body 내용이 다 빠져버리기 때문에
        // 실제 client에게 전달되는 response 내용은 비어있게 된다.
        String resContent = new String(httpServletResponse.getContentAsByteArray());
        int httpStatus = httpServletResponse.getStatus();

        // 위에서 getContentAsByteArray()로 인해 body 내용이 비었기 때문에
        // 아래 함수에서 다시 채워준다.
        httpServletResponse.copyBodyToResponse();

        log.info("response status: {}, response body : {}", httpStatus, resContent);
    }
}
