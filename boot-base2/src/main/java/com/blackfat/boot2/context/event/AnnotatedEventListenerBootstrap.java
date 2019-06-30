package com.blackfat.boot2.context.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-06-30 11:42
 * @since 1.0-SNAPSHOT
 */
public class AnnotatedEventListenerBootstrap {

    public static void main(String[] args) {
        // 创建 注解驱动 Spring 应用上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 @EventListener 类 MyEventListener
        context.register(MyEventListener.class);
        // 初始化上下文
        context.refresh();
        // 关闭上下文
        context.close();
    }

    /**
     * {@link EventListener} 抽象类
     */
    public static abstract class AbstractEventListener {
        @EventListener(ContextRefreshedEvent.class)
        public void onContextRefreshedEvent(ContextRefreshedEvent event) {
            System.out.println("AbstractEventListener : " + event.getClass().getSimpleName());
        }
    }

    /**
     * 具体 {@link EventListener}类，作为 Spring Bean，继承 {@link AbstractEventListener}
     */
    public static class MyEventListener extends AbstractEventListener {

        @EventListener(ContextClosedEvent.class)
        public boolean onContextClosedEvent(ContextClosedEvent event) {
            System.out.println("MyEventListener : " + event.getClass().getSimpleName());
            return true;
        }
    }
}
