package com.blackfat.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-02-14 16:34
 * @since 1.0-SNAPSHOT
 */
@Component
public class CircularDependencyA {

    @Autowired
    private CircularDependencyB circB;


    public CircularDependencyA() {
    }
}
