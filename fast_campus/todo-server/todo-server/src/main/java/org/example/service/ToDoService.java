package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.ToDoModel;
import org.example.model.ToDoRequest;
import org.example.repository.ToDoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// 서비스 layer에서는 작성한 repository가 실제로 동작하는 코드를 작성함.
@Service
@AllArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    // 1. todo 리스트 목록에 아이템을 추가
    // 2. todo 리스트 목록 중 특정 아이템을 조회
    // 3. todo 리스트 전체 목록을 조회
    // 4. todo 리스트 목록 중 특정 아이템을 수정
    // 5. todo 리스트 목록 중 특정 아이템을 삭제
    // 6. todo 리스트 전체 목록을 삭제

    public ToDoModel add(ToDoRequest request) {
        ToDoModel toDoModel = new ToDoModel();
        toDoModel.setTitle(request.getTitle());
        toDoModel.setOrder(request.getOrder());
        toDoModel.setCompleted(request.getCompleted());
        return this.toDoRepository.save(toDoModel);
    }

    public ToDoModel searchById(Long id) {
        return this.toDoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ToDoModel> searchAll() {
        return this.toDoRepository.findAll();
    }

    public ToDoModel updateById(Long id, ToDoRequest request) {
        ToDoModel toDoModel = this.searchById(id);
        if (request.getTitle() != null) {
            toDoModel.setTitle(request.getTitle());
        }
        if (request.getOrder() != null) {
            toDoModel.setOrder(request.getOrder());
        }
        if (request.getCompleted() != null) {
            toDoModel.setCompleted(request.getCompleted());
        }
        return this.toDoRepository.save(toDoModel);
    }

    public void deleteById(Long id) {
        this.toDoRepository.deleteById(id);
    }

    public void deleteAll() {
        this.toDoRepository.deleteAll();
    }

}
