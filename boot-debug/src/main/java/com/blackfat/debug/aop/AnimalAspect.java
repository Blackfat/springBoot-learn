package com.blackfat.debug.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-16 15:13
 * @since 1.0-SNAPSHOT
 */
@Aspect
@Component
public class AnimalAspect {

    @Pointcut("this(com.blackfat.debug.aop.AnimalService)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("begin ....");
        Object result = point.proceed();
        System.out.println("end ....");
        return result;
    }
}
