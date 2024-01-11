package com.example.dy.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect //aop 클래스 선언 = 부가기능을 주입하는 클래스
@Component // ioc 컨테이너가 해당 객체를 생성 및 관리
@Slf4j
public class DebuggingAspect {
    // 대상 메소드 선택: api 패키지의 모든 메서드
    @Pointcut("execution(* com.example.dy.api.*.*(..))") // commentservice 모든 서비스에 적용되어짐 *(..) 모든 메서드...
    private void cut(){


    }

    @Before("cut()")
    public void loggingArgs(JoinPoint joinPoint) { // cut()의 대상 메소드
        // 입력값 가져오기
        Object[] args = joinPoint.getArgs();
        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();
        // 메소드명
        String methodName = joinPoint.getSignature()
                .getName();
        // 입력값 로깅하기
        for (Object obj : args) { // foreach 문
            log.info("{}#{}의 입력값 => {}", className, methodName, obj);
        }
    }


    //실행 시점 설정: cut()에 지정된 대상 호출 성공 후!
    @AfterReturning(value = "cut()", returning = "returnObj") // 아래 리턴값을 넣어줌...
    public void loggingReturnValue(JoinPoint joinPoint, // cut()의 대상 메소드
                                   Object returnObj) { // 리턴값

        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();
        // 메소드명
        String methodName = joinPoint.getSignature()
                .getName();

        // 반환값 로깅
        log.info("{}#{}의 반환값 => {}", className, methodName, returnObj);
    }

}
