package com.blackfat.kernel.ability;

import com.blackfat.kernel.ability.core.ExtendAbility;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-15 17:21
 * @since 1.0-SNAPSHOT
 */
@ExtendAbility(name = "test", model = "test", process = "test")
public interface AbilityTest extends TestOperation1, TestOperation2 {
    @Override
    default Boolean operation1(TestContext context) {
        return true;
    }

    @Override
    default Boolean operation2(TestContext context) {
        return true;
    }
}
