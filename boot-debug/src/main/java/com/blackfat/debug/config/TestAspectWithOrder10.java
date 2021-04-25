package com.blackfat.debug.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-04-26 09:38
 * @since 1.0-SNAPSHOT
 */

/**
 *切点表达式函数：
 * 方法切点函数：
 * execution():满足某匹配模式的所有目标类方法的连接点
 * @annotation:标注了特定注解的目标方法的连接点
 *
 * 方法入参切点表达式：
 * args():通过判别方法运行时入参对象的类型定义指定连接点
 * @args():通过判别方法运行时入参对象是否标注了特定的注解来指定连接点
 *
 * 目标类切点函数：
 * within():特定类型内的方法执行
 * @within():目标类含有指定注解，则目标类及其实现类的所有连接点都匹配
 * @target():目标类含有指定注解，则目标类的所有连接点都匹配
 *
 * 切点函数入参中使用通配符：
 * *：匹配任意字符，但只能匹配一个元素
 * ..:匹配任意字符，可以匹配多个元素，凡表示类时，必须和*配合使用，表示入参是可以单独使用
 * +：表示按类型匹配指定类的所有类（com.spring.Waiter+,继承或扩展指定类的所有类，同时还包括指定类
 * ）
 * 在类名模式中，".*"表示包下的所有类，"..*"表示包及其子包下的所有类
 */
@Aspect
@Component
@Order(10)
@Slf4j
public class TestAspectWithOrder10 {
    @Before("execution(* com.blackfat.debug.**.TestAopController.*(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.info("TestAspectWithOrder10 @Before");
    }

    @After("execution(* com.blackfat.debug.**.TestAopController.*(..))")
    public void after(JoinPoint joinPoint) throws Throwable {
        log.info("TestAspectWithOrder10 @After");
    }

    @Around("execution(* com.blackfat.debug.**.TestAopController.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("TestAspectWithOrder10 @Around before");
        Object o = pjp.proceed();
        log.info("TestAspectWithOrder10 @Around after");
        return o;
    }
}


