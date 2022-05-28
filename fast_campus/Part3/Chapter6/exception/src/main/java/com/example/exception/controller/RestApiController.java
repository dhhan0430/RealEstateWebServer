package com.example.exception.controller;

import com.example.exception.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/user")
@Validated
public class RestApiController {

    @GetMapping("")
    // required = false : 해당 파라미터가 없더라도 실행 가능. ?name=
    public User get(
            @Size(min = 2)
            @RequestParam String name,
            @NotNull
            @Min(1)
            @RequestParam Integer age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);

        // int a = 10 + age; // RequestParam에 null이 들어오면 exception 발생하게 됨.

        return user;
    }

    @PostMapping("")
    public User post(@Valid @RequestBody User user) {
        System.out.println(user);
        return user;
    }

    // ExceptionHandler를 여기에 놓으면, 이 Controller 에서 발생하는 예외만 핸들링함.
    // GlobalControllerAdvice와 여기에 둘 다 있으면, 여기 local에 있는 것만 우선으로 동작함.
    /*
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity
    methodArgumentNotValidException(MethodArgumentNotValidException e) {
        System.out.println("local");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    */
}
