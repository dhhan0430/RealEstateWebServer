/*
package com.example.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// controller 패키지 하위의 예외들을 모두 잡을 것이다.
// @RestControllerAdvice(basePackages = "com.example.exception.controller")
// 전체적으로 Exception을 다 잡을 것이다.
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = Exception.class) // value: 모든 예외를 잡는다고 선언함.
    public ResponseEntity exception(Exception e) {
        System.out.println(e.getClass().getName());
        System.out.println("--------------------");
        System.out.println(e.getLocalizedMessage());
        System.out.println("--------------------");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity
    methodArgumentNotValidException(MethodArgumentNotValidException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
*/