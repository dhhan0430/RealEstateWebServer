package com.example.aop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
// @Bean: class에 못 사용하고, 메서드에만 사용 가능.
// @Configuration: 하나의 클래스에 여러 개의 Bean 등록해주는 것.
public class TimerAop {

    @Pointcut("execution(* com.example.aop.controller..*.*(..))")
    private void cut() {

    }

    // 내가 만든 Timer annotation이 지정된 곳에 적용하겠다.
    @Pointcut("@annotation(com.example.aop.annotation.Timer)")
    private void enableTimer() {

    }

    // 이렇게 aop를 사용하지 않으면, RestApiController 클래스의 GetMapping, PostMapping
    // 등의 메서드 내부에 실제 서비스 로직과는 상관 없는 시간을 계산하는 코드들을 계속 붙여야 한다.
    @Around("cut() && enableTimer()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // proceed()를 호출하면 실질적으로 메서드가 실행이 됨.
        // 만약 리턴 타입이 있다면 object로 리턴된다.
        Object result = joinPoint.proceed();

        stopWatch.stop();

        System.out.println("total time: " + stopWatch.getTotalTimeSeconds());
    }
}
