package com.blackfat.springboot.druid.service.impl;


import com.blackfat.springboot.druid.dao.StockMapper;
import com.blackfat.springboot.druid.entity.Stock;
import com.blackfat.springboot.druid.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/10/23-15:11
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public int getStockCount(int id) {
        Stock ssmStock = stockMapper.selectByPrimaryKey(id);
        return ssmStock.getCount();
    }

    @Override
    public Stock getStockById(int id) {
        return stockMapper.selectByPrimaryKey(id) ;
    }

    @Override
    @Transactional
    public int updateStockByOptimistic(Stock stock) {
        return stockMapper.updateByOptimistic(stock);
    }
}
