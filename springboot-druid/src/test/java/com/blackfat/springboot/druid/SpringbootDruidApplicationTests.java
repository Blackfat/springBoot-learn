package com.blackfat.springboot.druid;

import com.blackfat.springboot.druid.dao.StockMapper;
import com.blackfat.springboot.druid.entity.Stock;
import com.blackfat.springboot.druid.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDruidApplicationTests {

	@Autowired
	StockMapper stockMapper;

	@Autowired
	OrderService orderService;


	@Test
	public void stockMapperTest(){
		Stock stock = stockMapper.selectByPrimaryKey(1);
		System.out.println(stock.toString());
	}

	@Test
	public void orderServiceTest() throws Exception {
	    orderService.createOptimisticOrder(1);
    }

}
