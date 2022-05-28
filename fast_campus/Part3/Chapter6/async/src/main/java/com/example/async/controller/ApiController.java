package com.example.async.controller;

import com.example.async.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    private final AsyncService asyncService;

    public ApiController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    /*
    @GetMapping("/hello")
    public String hello() {

        asyncService.hello();
        log.info("method end");
        return "hello";
    }
    */

    @GetMapping("/hello")
    // CompletableFuture 는 다른 쓰레드에서 실행시키고 결과를 반환 받는 형태.
    public CompletableFuture hello() {
        log.info("completable future init");
        return asyncService.run();
    }

}
