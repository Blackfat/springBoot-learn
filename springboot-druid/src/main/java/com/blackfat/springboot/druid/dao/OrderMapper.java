package com.blackfat.springboot.druid.dao;


import com.blackfat.springboot.druid.entity.StockOrder;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/10/23-14:37
 */
public interface OrderMapper {

    int insertSelective(StockOrder order);
}
