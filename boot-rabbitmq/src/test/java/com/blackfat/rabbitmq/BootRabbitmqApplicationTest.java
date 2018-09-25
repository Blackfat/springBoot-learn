package com.blackfat.rabbitmq;

import com.blackfat.BootRabbitmqApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/9/19-15:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= BootRabbitmqApplication.class)
public class BootRabbitmqApplicationTest {

    @Autowired
    private Sender sender;

    @Test
    public void hello() throws Exception {
        sender.send();
    }


}
