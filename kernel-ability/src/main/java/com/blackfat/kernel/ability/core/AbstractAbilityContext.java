package com.blackfat.kernel.ability.core;

import lombok.Data;


@Data
public abstract class AbstractAbilityContext implements AbilityBusinessContextAble {
    /**
     * 产品ID
     */
    private String productId;

    /**
     * 获取能力匹配系统使用的业务id，默认为产品id，可以覆盖
     *
     * @return 业务id
     */
    @Override
    public String getAbilityBusinessId() {
        return this.productId;
    }
}

