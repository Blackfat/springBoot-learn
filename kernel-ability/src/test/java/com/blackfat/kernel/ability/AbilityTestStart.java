package com.blackfat.kernel.ability;

import com.blackfat.kernel.ability.factory.ExtendAbilityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ExtendAbilityScan(basePackages = "com.blackfat.kernel.ability.**")
@ComponentScan(basePackages = {"com.blackfat.kernel.ability.**"})
public class AbilityTestStart {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AbilityTestStart.class, args);

        EngineTest bean = context.getBean(EngineTest.class);

        bean.test();
    }
}
