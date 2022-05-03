package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.ToDoModel;
import org.example.model.ToDoRequest;
import org.example.service.ToDoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ToDoController.class)
class ToDoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ToDoService toDoService;

    private ToDoModel expected;

    @BeforeEach
    void setup() {
        this.expected = new ToDoModel();
        this.expected.setId(123L);
        this.expected.setTitle("TEST TITLE");
        this.expected.setOrder(0L);
        this.expected.setCompleted(false);

    }

    @Test
    void create() throws Exception {
        // toDoService에서 add하면, 그걸 기반으로 ToDoEntity를 만든다.
        when(this.toDoService.add(any(ToDoRequest.class)))
                .then((i) -> {
                    ToDoRequest request = i.getArgument(0, ToDoRequest.class);
                    // title만 request로 들어온 값으로 반환해준다.
                    return new ToDoModel(this.expected.getId(),
                                            request.getTitle(),
                                            this.expected.getOrder(),
                                            this.expected.getCompleted());
                });

        ToDoRequest request = new ToDoRequest();
        request.setTitle("ANY TITLE");

        // 위 request 기반으로 content 만든다.
        // ObjectMapper를 사용하여 request body를 만든다.
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        this.mvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("ANY TITLE"));
    }

    @Test
    void readOne() {
    }
}