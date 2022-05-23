package com.example.validation.controller;

import com.example.validation.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiController {

    // BindingResult bindingResult 를 사용하게 되면, @Valid 에 대한 결과가
    // bindingResult 에 들어오게 됨.
    @PostMapping("/user")
    public Object user(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            bindingResult.getAllErrors().forEach(objectError -> {
                // FieldError 클래스가 objectError 의 클래스보다 child.
                FieldError field = (FieldError)objectError;
                String message = objectError.getDefaultMessage();

                System.out.println("field: " + field.getField());
                System.out.println(message);

                sb.append("field: " + field.getField());
                sb.append("message: " + message + "\n");
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        // LOGIC 작성.

        System.out.println(user);
        return user;

    }
}
