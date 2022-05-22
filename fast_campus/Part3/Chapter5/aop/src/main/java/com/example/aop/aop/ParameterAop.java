package com.example.aop.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

// 이렇게 aop를 사용하면, 내 서버 외부에 어떤 값들이 요청으로 들어왔는지, 그리고 내 서버가 어떤 값들을
// 리턴해줬는지 디버깅할 때 편하게 알 수 있다.

@Aspect    // aop 사용
@Component
public class ParameterAop {

    // controller package 하위에 있는 모든 메서드를 aop로 보겠다.
    @Pointcut("execution(* com.example.aop.controller..*.*(..))")
    private void cut() {

    }

    // @Pointcut 하위에 있는 메서드들이 실행되기 전에 아래 메서드가 실행될 것이고,
    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println(method.getName());

        Object[] args = joinPoint.getArgs();

        for(Object obj : args) {
            System.out.println("type: " + obj.getClass().getSimpleName());
            System.out.println("value: " + obj);
        }
    }

    // @Pointcut 하위에 있는 메서드들이 실행된 후에 정상 실행을 하고 return을 해주게 되면,
    // 해당 returnObj 값을 아래에서 볼 수 있다.
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturn(JoinPoint joinPoint, Object returnObj) {
        System.out.println("return obj");
        System.out.println(returnObj);
    }
}
