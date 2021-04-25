package com.blackfat.kernel.ability.entity;

import com.blackfat.kernel.ability.core.ExtendAbilityOperation;
import lombok.Data;


@Data
public class OperationEntity {
    /**
     * 操作名
     */
    private String name;

    /**
     * 对应class
     */
    private Class<?> operationInterface;

    public OperationEntity(Class<?> operationInterface, ExtendAbilityOperation operation) {
        this.operationInterface = operationInterface;
        this.name = operation.name();
    }
}

