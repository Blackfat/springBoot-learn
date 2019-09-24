package com.blackfat.bootdrools;

import com.blackfat.bootdrools.entity.Product;
import com.blackfat.bootdrools.entity.User;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.event.DefaultAgendaEventListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootDroolsApplicationTests {

    @Autowired
    KieSession kieSession;

    @Test
    public void testHelloWord() {
        kieSession.fireAllRules();
    }

    @Test
    public void testUser(){
        User user=new User("张三",18);
        // 向规则文件中传入参数
        kieSession.insert(user);
        // 指定执行的规则名称
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("user"));
        System.err.println("规则执行完毕后张三变为了："+user.getName());
    }

    @Test
    public void testContains() {
        String name="张三";
        User user=new User("张三",18);
        kieSession.insert(name);
        kieSession.insert(user);
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("contains"));
    }
    @Test
    public void testMemberOf() {
        List list=new ArrayList();
        list.add("张三");
        list.add("李四");
        User user=new User("李四",18);
        kieSession.insert(list);
        kieSession.insert(user);
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("memberOf"));
    }
    @Test
    public void testMatches() {
        User user=new User("张三",18);
        kieSession.insert(user);
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("matches"));
    }

    @Test
    public void testProduct(){
        Product fan = new Product("电扇", 280);
        Product washer = new Product("洗衣机",2200);
        Product phone = new Product("手机", 998);
        kieSession.insert(fan);
        kieSession.insert(washer);
        kieSession.insert(phone);
        kieSession.fireAllRules();
    }


    @Test
    public void testAgendaEventListener(){
        kieSession.addEventListener(new DefaultAgendaEventListener(){
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                super.afterMatchFired(event);
                System.out.println("命中:"+event.getMatch().getRule().getName());
            }
        });
        Product fan = new Product("电扇", 280);
        kieSession.insert(fan);
        kieSession.fireAllRules();
    }




}
