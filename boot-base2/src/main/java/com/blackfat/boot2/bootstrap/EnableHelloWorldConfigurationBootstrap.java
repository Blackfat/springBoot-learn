package com.blackfat.boot2.bootstrap;

import com.blackfat.boot2.annoation.EnableHelloWorld;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-25 14:54
 * @since 1.0-SNAPSHOT
 */
@EnableHelloWorld
public class EnableHelloWorldConfigurationBootstrap {

    public static void main(String[] args) {
        // 构建 Annotation 配置驱动 Spring 上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 当前引导类（被 @Configuration 标注） 到 Spring 上下文
        context.register(EnableHelloWorldConfigurationBootstrap.class);
        // 启动上下文
        context.refresh();
        // 获取名称为 "helloWorld" Bean 对象
        String helloWorld = context.getBean("helloWorld", String.class);
        // 输出用户名称："Hello,World"
        System.out.printf("helloWorld = %s \n", helloWorld);
        // 关闭上下文
        context.close();
    }
}
