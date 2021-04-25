package com.blackfat.kernel.ability;


import com.blackfat.kernel.ability.core.ExtendAbilityOperation;

@ExtendAbilityOperation(name = "当期还款检查")
public interface TestOperation1 {
    /**
     * test
     *
     * @param context
     * @return
     */
    Boolean operation1(TestContext context);

}
