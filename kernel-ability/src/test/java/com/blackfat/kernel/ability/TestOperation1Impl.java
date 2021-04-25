package com.blackfat.kernel.ability;

import com.blackfat.kernel.ability.core.ExtendAbilityOperationImpl;
import net.bytebuddy.ByteBuddy;


@ExtendAbilityOperationImpl(operationCode = "test", name = "for test")
public class TestOperation1Impl implements TestOperation1 {
    @Override
    public Boolean operation1(TestContext context) {
        return Boolean.FALSE;
    }



    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class<?> dynamicType = new ByteBuddy().
                // 父类
                subclass(Object.class)
                // 实现接口
                .implement(AbilityTest.class)
                // 类名称
                .name(AbilityTest.class.getName() + "Impl")
                // 生成字节码
                .make()
                // 获得class对象
                .load(AbilityTest.class.getClassLoader()).getLoaded();

        AbilityTest abilityTest = (AbilityTest)dynamicType.newInstance();

        System.out.println(abilityTest.operation1(null));
        System.out.println(abilityTest.operation2(null));

    }
}
