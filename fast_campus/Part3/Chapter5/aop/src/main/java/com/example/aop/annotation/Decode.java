package com.example.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Timer에 대한 target을 type과 method로 지정.
@Target({ElementType.TYPE, ElementType.METHOD})
// runtime 때 사용.
@Retention(RetentionPolicy.RUNTIME)
public @interface Decode {
}
