package com.blackfat.kernel.ability.entity;

import com.blackfat.kernel.ability.core.ExtendAbilityOperationImpl;
import lombok.Data;


@Data
public class OperationImplEntity {
    private String operationCode;
    private String name;
    private String desc;

    public OperationImplEntity(ExtendAbilityOperationImpl operation) {
        this.operationCode = operation.operationCode();
        this.desc = operation.desc();
        this.name = operation.name();
    }
}
