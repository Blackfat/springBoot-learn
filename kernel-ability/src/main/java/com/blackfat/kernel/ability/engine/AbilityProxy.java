package com.blackfat.kernel.ability.engine;

import com.blackfat.kernel.ability.core.AbilityBusinessContextAble;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * 每个 ability 的实际代理类
 * @param <T>
 */
@Slf4j
public class AbilityProxy<T> implements MethodInterceptor {

    private AbilityEngine abilityEngine;

    private final Class<T> abilityInterface;

    public AbilityProxy(AbilityEngine abilityEngine, Class<T> abilityInterface) {
        this.abilityEngine = abilityEngine;
        this.abilityInterface = abilityInterface;
        this.abilityEngine.addAbilityInterface(abilityInterface);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        if (Object.class.equals(method.getDeclaringClass())) {
            return methodProxy.invokeSuper(o, objects);
        }
        // 参数解析为 AbilityBusinessContextAble
        if (objects == null || objects.length <= 0) {
            log.warn("no param defined by this method {}", method);
            return methodProxy.invokeSuper(o, objects);
        }

        // 从engine 查询具体对应实现，invoke
        Object param1 = objects[0];
        if (param1 instanceof AbilityBusinessContextAble) {
            AbilityBusinessContextAble context = (AbilityBusinessContextAble)param1;
            String productId = context.getAbilityBusinessId();
            String methodName = method.getName();
            Object ability = abilityEngine.getAbility(productId, this.abilityInterface, methodName);
            if (ability != null) {
                log.debug("product {} methodName {} use operation {}", productId, methodName, ability);
                return MethodUtils.invokeExactMethod(ability, methodName, objects);
            } else {
                log.debug("product {} methodName {} use default", productId, methodName);
            }
        } else {
            log.warn("first param must be AbilityBusinessContextAble {}", method);
        }
        return methodProxy.invokeSuper(o, objects);

    }
}

