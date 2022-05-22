package com.example.response.controller;

import com.example.response.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST API를 작성하는 서버

@RestController
@RequestMapping("/api")
public class ApiController {

    // TEXT 를 response 해줌.
    @GetMapping("/text")
    public String text(@RequestParam String account) {

        return account;
    }


    // REST API에서 JSON 사용방법 2가지: 아래 2가지 방법 참고.
    // req -> object mapper -> object -> method -> object mapper -> json -> res
    @PostMapping("/json")
    public User json(@RequestBody User user) {
        return user;
    }

    // put을 해서 리소스 생성에 성공하면 201 OK는 어떻게 Response 해주나?
    // => ResponseEntity 사용
    @PutMapping("/put")
    public ResponseEntity<User> put(@RequestBody User user) {

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
