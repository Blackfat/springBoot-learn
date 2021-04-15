package com.blackfat.boot2;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-01-12 10:14
 * @since 1.0-SNAPSHOT
 */
public class DefaultListableBeanFactoryTest {

    public static void main(String[] args) {
        //创建一个DefaultListableBeanFactory实例
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //创建一个BeanDefinition
        RootBeanDefinition beanDefinition = new RootBeanDefinition(Wheel.class);
        //将BeanDefinition注册到容器中
        beanFactory.registerBeanDefinition("wheel", beanDefinition);
        // getBean
        Wheel wheel = beanFactory.getBean("wheel", Wheel.class);
    }

    public static class Wheel {

    }
}


