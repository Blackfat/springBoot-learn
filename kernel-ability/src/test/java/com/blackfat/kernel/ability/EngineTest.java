package com.blackfat.kernel.ability;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class EngineTest {

    @Resource
    private AbilityTest abilityTest;

    public void test() {
        TestContext testContext = new TestContext();
        testContext.setProductId("test");
        log.info(JSON.toJSONString(abilityTest.operation1(testContext)));
        log.info(JSON.toJSONString(abilityTest.operation2(testContext)));
    }
}
