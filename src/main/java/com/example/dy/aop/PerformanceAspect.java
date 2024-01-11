package com.example.dy.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect //aop 지정
@Component
@Slf4j
public class PerformanceAspect {

    // 특정 어노테이션을 대상 지정
    @Pointcut("@annotation(com.example.dy.annotation.RunningTime)")
    private void enableRunningTime() {}

    // 기본 패키지의 모든 메소드
    @Pointcut("execution(* com.example.dy..*.*(..))") // dy의 모든 메소드를 지정한다.
    private void cut() {}

    // 두개다 짬뽕한다
    // Around 실행 시점 설정 : 두 조건을 모두 만족하는 대상을 전후로 부가 기능을 삽입
    @Around("cut() && enableRunningTime()")
    public void loggingRunningTime(ProceedingJoinPoint joinPoint) throws Throwable { // ProceedingJoinPoint 실행까지하는 조인포인트

        // 메소드 수행 전
        StopWatch stopWatch = new StopWatch(); // 시간측정 stopWatch
        stopWatch.start();                     // 측정 시작
        // 메소드 수행
        Object returningObj = joinPoint.proceed();
        // 메소드 종료 후
        stopWatch.stop();                      // 종료
        String methodName = joinPoint.getSignature()
                .getName();
        log.info("{}의 총 수행 시간 => {} sec", methodName, stopWatch.getTotalTimeSeconds()); // 총 시간 반환
    }
}