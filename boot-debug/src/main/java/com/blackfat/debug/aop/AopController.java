package com.blackfat.debug.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-16 15:18
 * @since 1.0-SNAPSHOT
 */
@RestController
@RequestMapping("/aop1")
public class AopController {

    @Resource
    private AnimalService animalService;

    @GetMapping("/")
    public String aop(){
        animalService.say();
        return "hello";
    }

}
