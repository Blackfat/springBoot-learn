package com.blackfat.boot2.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author wangfeiyang
 * @Description 监听自定义事件引导类，通过 {@link GenericApplicationContext#registerBean(Class, BeanDefinitionCustomizer...)} 方法注册
 * {@link ApplicationListener} Bean
 * @create 2019-06-30 11:13
 * @since 1.0-SNAPSHOT
 */
public class ApplicationListenerBeanOnCustomEventBootstrap {


    public static void main(String[] args) {
        // 创建spring应用上下文
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(MyApplicationListen.class);
        context.refresh();

        // 发布事件
        context.publishEvent(new MyApplicationEvent("hello world"));

        context.close();

        context.publishEvent(new MyApplicationEvent("hello world again"));
    }

    public static class MyApplicationEvent extends ApplicationEvent {

        public MyApplicationEvent(Object source) {
            super(source);
        }
    }

    public static class MyApplicationListen implements ApplicationListener<MyApplicationEvent> {

        @Override
        public void onApplicationEvent(MyApplicationEvent myApplicationEvent) {
            System.out.println(myApplicationEvent.getClass().getSimpleName());
        }

    }


}
