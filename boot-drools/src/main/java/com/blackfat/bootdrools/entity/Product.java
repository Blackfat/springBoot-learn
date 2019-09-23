package com.blackfat.bootdrools.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-09-23 15:52
 * @since 1.0-SNAPSHOT
 */
@Data
@NoArgsConstructor
public class Product {

    // 商品名称
    private String name;
    // 商品定价
    private double prePrice;
    //商品实际售价
    private double realPrice;

    public Product(String name, double prePrice) {
        this.name = name;
        this.prePrice = prePrice;
    }
}
