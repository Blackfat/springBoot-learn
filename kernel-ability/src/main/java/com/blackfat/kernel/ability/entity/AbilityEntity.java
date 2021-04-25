package com.blackfat.kernel.ability.entity;

import com.blackfat.kernel.ability.core.ExtendAbility;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Data
@Accessors(chain = true)
@Slf4j
public class AbilityEntity {
    /**
     * 能力名
     */
    private String name;
    /**
     * 模块
     */
    private String model;
    /**
     * 流程
     */
    private String process;

    /**
     * 方法名对应操作的映射
     */
    private Map<String, OperationEntity> methodOperationMap = new HashMap<>(10);
    /**
     * 对应的接口class
     */
    private Class<?> abilityInterface;

    /**
     * 操作列表
     */
    private Set<OperationEntity> operationEntities = new HashSet<>(3);

    public AbilityEntity setByAnnotation(ExtendAbility annotation) {
        this.setModel(annotation.model());
        this.setName(annotation.name());
        this.setProcess(annotation.process());
        return this;
    }

    /**
     * 增加 方法名对应的 操作类型
     *
     * @param name 方法名
     * @param entity 操作实体
     */
    public void addMethodOperation(String name, OperationEntity entity) {
        OperationEntity value = this.methodOperationMap.get(name);
        if (value != null) {
            log.error("ability method can not be same {} {} {}", abilityInterface, name, methodOperationMap.get(name));
        } else {
            this.methodOperationMap.put(name, entity);
        }
        this.operationEntities.add(entity);
    }
}
