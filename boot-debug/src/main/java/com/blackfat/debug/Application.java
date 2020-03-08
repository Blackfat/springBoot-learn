package com.blackfat.debug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
* @Configuration 等价与XML中配置beans
* @EnableAutoConfiguration 隐式定义了一个基础“search package”去查找一些具体的注解，根据添加的jar依赖猜测你想如何配置Spring
* @ComponentScan 定义一个basePackage属性,默认使用根包(main方法所在的包) 注解搜索beans，并结合 @Autowired 构造器注入
**/
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
