package com.blackfat.debug;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-04-26 09:36
 * @since 1.0-SNAPSHOT
 */

/**
 * 入操作（Around（连接点执行前）、Before），切面优先级越高，越先执行。一个切面的入操作执行完，才轮到下一切面，所有切面入操作执行完，才开始执行连接点（方法）。
 * 出操作（Around（连接点执行后）、After、AfterReturning、AfterThrowing），切面优先级越低，越先执行。
 * 一个切面的出操作执行完，才轮到下一切面，直到返回到调用点。同一切面的 Around 比 After、Before 先执行。
 */
@RestController
@RequestMapping("/aop")
public class TestAopController {


    @GetMapping("/")
    public String list() {
        return "hello";
    }
}
