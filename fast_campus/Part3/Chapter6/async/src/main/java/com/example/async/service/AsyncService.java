package com.example.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncService {

    public String hello() {

        for (int i=0; i<10; i++) {
            try {
                Thread.sleep(2000);
                log.info("thread sleep ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "async hello";
    }

    // Async 는 aop 기반이기 때문에 proxy 패턴을 타게된다.
    // 그래서 public method 에만 Async 적용 가능.
    @Async("async-thread")
    public CompletableFuture run() {

        // 여기서 바로 hello() 호출하면 Async 동작하지 않는다.
        // 같은 클래스 내에 있는 메서드를 호출할 때는 async 를 타지 않는다.
        // hello();
        return new AsyncResult(hello()).completable();
    }


}
