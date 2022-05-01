package org.example.service;

import org.example.model.ToDoModel;
import org.example.model.ToDoRequest;
import org.example.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

    // mock 쓰는 이유싯
    // 1. 외부 시스템에 의존하지 않고 자체 테스트를 실행하기 위해.
    // => 데이터베이스나, 네트워크가 연결되지 않은 상황에서 유닛 테스트 가능하기 위해.
    // 2. 실제 데이터베이스를 사용하게 되면 안되기 때문. 데이트베이스에는 민감한 정보들이 많기 때문.
    @Mock
    private ToDoRepository toDoRepository;

    @InjectMocks
    private ToDoService toDoService;

    @Test
    void add() {
        // toDoRepository가 ToDoEntity를 저장할 때, 받은 entity 값을 반환하도록 설정.
        when(this.toDoRepository.save(any(ToDoModel.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ToDoRequest expected = new ToDoRequest();
        expected.setTitle("Test Title");

        ToDoModel actual = this.toDoService.add(expected);

        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void searchById() {
        ToDoModel entity = new ToDoModel();
        entity.setId(123L);
        entity.setTitle("TITLE");
        entity.setOrder(0L);
        entity.setCompleted(false);
        // 위 정보들로 세팅한 entity를 통해 optional을 만든다.
        Optional<ToDoModel> optional = Optional.of(entity);

        // toDoRepository에서 findById가 어떤 long타입에 대해서 불렸을 때
        // optional 리턴하도록 해놓음.
        given(this.toDoRepository.findById(anyLong()))
                .willReturn(optional);

        ToDoModel actual = this.toDoService.searchById(123L);
        // 내가 기대하는 값.
        ToDoModel expected = optional.get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());
    }

    // 값이 없는 값을 조회할 때도 에러를 발생시키는데, 정말로 에러가 잘 발생하는지에 대한 테스트.
    @Test
    public void searchByIdFailed() {
        // toDoRepository에 어느 값이든 조회를 했을 때 무조건 empty를 리턴하게 해놓음.
        given(this.toDoRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // searchById 했을 때 에러 발생하도록 함.
        assertThrows(ResponseStatusException.class, () -> {
            this.toDoService.searchById(123L);
        });
    }
}