package com.fastcampus.jpa.bookmanager2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// WebMvcTest 는 슬라이스 테스트라고 한다. 전체 스프링 컨텍스트를 로딩하지 않고,
// 웹 컨트롤러에 대한 일부 bean 들만 로딩을 해서 테스트 한다.
// 그렇기 때문에 "JPA metamodel must not be empty"는 jpa 옵션에서 발생하는 문제.
// 이에 대한 해결 방법은 총 3가지
// 1. @MockBean(JpaMetamodelMappingContext.class) 적용.
// => 왜냐하면 helloWorld() 컨트롤러 테스트에서는 jpa 가 필요 없는데, 필요 없기 때문에
// 로딩을 할 수 없었던 것인데, MockBean 을 통해 마치 있는 것처럼 동작시키는 것이다.
// 2. BookManagerApplication 클래스에 @EnableJpaAuditing 을 했었는데,
// JpaConfiguration 이라는 클래스를 별도로 생성을 해서 @Configuration, @EnableJpaAuditing 을
// 을 적용하여 별도의 Bean에서 로딩시켜주는 방식
// 3. WebMvcTest 처럼 slice 테스트를 사용하는 것이 아니라,
// full test(@SpringBootTest)를 사용한다.
// 이 때는 @Autowired private MockMvc mockMvc; 를 지우고
// @Autowired private WebApplicationContext(전체 web app 컨텍스트) wac; 를 적용한다.
// 아래 주석 참고

// @MockBean(JpaMetamodelMappingContext.class)
// @SpringBootTest
@WebMvcTest
class HelloWorldControllerTest {

    /* 방법 3. WebMvcTest 에서 자동으로 만들어줬던 MockMvc 를 직접 만들어주는 방법이다.
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    */

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloWorld() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello-world"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello-world"));
    }
}