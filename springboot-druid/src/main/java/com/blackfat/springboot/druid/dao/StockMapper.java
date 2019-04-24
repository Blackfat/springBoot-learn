package com.blackfat.springboot.druid.dao;


import com.blackfat.springboot.druid.entity.Stock;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/10/23-14:37
 */
public interface StockMapper {

    Stock selectByPrimaryKey(Integer id);


    int updateByOptimistic(Stock stock);


}
