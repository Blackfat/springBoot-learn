package com.blackfat.debug.aop;

import org.springframework.stereotype.Service;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-16 15:13
 * @since 1.0-SNAPSHOT
 */
@Service("dogService")
public class DogService implements AnimalService {

    @Override
    public void say() {
        System.out.println("bark .....");
    }
}
