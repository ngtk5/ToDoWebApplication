package com.ngtk5.todoapp.commons;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class EventLogFilter {

    @Before("execution(* com.ngtk5.todoapp..*.*(..))")
    public void beforeLog(JoinPoint joinPoint) {
        // 全てのメソッド呼び出しに前処理として実行される
        log.info(joinPoint.getSignature().getDeclaringType().getSimpleName() + "クラスの" + joinPoint.getSignature().getName() + "メソッドを開始します");
    }

    @After("execution(* com.ngtk5.todoapp..*.*(..))")
    public void afterLog(JoinPoint joinPoint) {
        // 全てのメソッド呼び出しに後処理として実行される
        log.info(joinPoint.getSignature().getDeclaringType().getSimpleName() + "クラスの" + joinPoint.getSignature().getName() + "メソッドを終了します");
    }
}