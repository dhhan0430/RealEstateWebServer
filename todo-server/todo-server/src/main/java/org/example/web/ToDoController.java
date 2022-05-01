package org.example.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ToDoModel;
import org.example.model.ToDoRequest;
import org.example.model.ToDoResponse;
import org.example.service.ToDoService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class ToDoController {

    private final ToDoService service;

    @PostMapping
    public ResponseEntity<ToDoResponse> create(@RequestBody ToDoRequest request) {
        log.info("CREATE");

        if (ObjectUtils.isEmpty(request.getTitle()))
            return ResponseEntity.badRequest().build();

        if (ObjectUtils.isEmpty(request.getOrder()))
            request.setOrder(0L);

        if (ObjectUtils.isEmpty(request.getCompleted()))
            request.setCompleted(false);

        ToDoModel result = this.service.add(request);

        return ResponseEntity.ok(new ToDoResponse(result));
    }

    @GetMapping("{id}")
    public ResponseEntity<ToDoResponse> readOne(@PathVariable Long id) {
        log.info("READ ONE");
        ToDoModel result = this.service.searchById(id);
        return ResponseEntity.ok(new ToDoResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<ToDoResponse>> readAll() {
        log.info("READ ALL");
        List<ToDoModel> list = this.service.searchAll();
        // ToDoEntity로 ToDoResponse를 생성.
        List<ToDoResponse> response = list.stream().map(ToDoResponse::new)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ToDoResponse> update(@PathVariable Long id,
                                               @RequestBody ToDoRequest request) {
        log.info("UPDATE");
        ToDoModel result = this.service.updateById(id, request);
        return ResponseEntity.ok(new ToDoResponse(result));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        log.info("DELETE");
        this.service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        log.info("DELETE ALL");
        this.service.deleteAll();
        return ResponseEntity.ok().build();
    }
}
