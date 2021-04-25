package com.blackfat.kernel.ability.factory;

import com.blackfat.kernel.ability.engine.AbilityEngine;
import com.blackfat.kernel.ability.engine.AbilityProxy;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Lazy;

@Slf4j
public class AbilityFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> abilityInterface;

    @Autowired
    @Lazy
    private AbilityEngine abilityEngine;

    private volatile T ins;

    public AbilityFactoryBean(Class<T> abilityInterface) {
        this.abilityInterface = abilityInterface;
    }

    public void setAbilityEngine(AbilityEngine abilityEngine) {
        this.abilityEngine = abilityEngine;
    }

    @Override
    public T getObject() throws Exception {
        synchronized (this.abilityInterface) {
            if (ins == null) {
                ins = newInstance();
            }
        }
        return ins;
    }

    @Override
    public Class<?> getObjectType() {
        return this.abilityInterface;
    }

    protected T newInstance() {
        log.info("create ability instance {} by {}", this.abilityInterface, this);
        // 生成接口的实现
        Class<?> dynamicType = new ByteBuddy().subclass(Object.class).implement(this.abilityInterface)
                .name(this.abilityInterface.getName() + "Impl").make().load(getClass().getClassLoader()).getLoaded();

        AbilityProxy<T> abilityProxy = new AbilityProxy<>(abilityEngine, abilityInterface);

        // 接口实现代理
        Enhancer e = new Enhancer();
        e.setSuperclass(dynamicType);
        e.setCallback(abilityProxy);
        return (T)e.create();

    }

}

