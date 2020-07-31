package com.v.component.rule.engine.scheduler;

import com.v.component.rule.engine.core.ExecuteResult;
import com.v.component.rule.engine.core.RuleSchedulerBuilder;
import com.v.component.rule.engine.rules.NormalStatusCheck;
import com.v.component.rule.engine.rules.TimeRangeCheck;
import org.jeasy.rules.api.Facts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 18:18
 * @description : 规则调度器功能测试
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("规则调度器功能测试")
public class DemoSchedulerTest
{
    @Autowired
    TimeRangeCheck timeRangeCheck;

    @Autowired
    NormalStatusCheck normalStatusCheck;

    @Autowired
    DemoScheduler demoScheduler;

    @Autowired
    RuleSchedulerBuilder ruleSchedulerBuilder;

    long one_day = 24 * 60 * 60 * 1000L;

    @Test
    @DisplayName("时间范围检查测试")
    void timeRangeCheckTest()
    {
        Date now = new Date();
        Facts facts = new Facts();
        facts.put("beginTime", now.getTime() - one_day * 3);
        facts.put("endTime", now.getTime() + one_day * 4);

        DemoScheduler scheduler = (DemoScheduler) ruleSchedulerBuilder
                .newScheduler(DemoScheduler.class).withRulesEngine()
                .withRule(timeRangeCheck).withFacts(facts).build();

        ExecuteResult executeResult = scheduler.fire();
        System.out.println("规则执行结果:" + executeResult);

        Assertions.assertNotNull(executeResult);
        Assertions.assertEquals(ExecuteResult.State.SUCCESS, executeResult.getState());
    }

    @Test
    @DisplayName("时间加状态检查测试")
    void timeAndStatusTest()
    {
        Date now = new Date();
        Facts facts = new Facts();
        facts.put("beginTime", now.getTime() - one_day * 3);
        facts.put("endTime", now.getTime() + one_day * 4);
        facts.put("status", -1);

        DemoScheduler scheduler = (DemoScheduler) ruleSchedulerBuilder
                .newScheduler(DemoScheduler.class).withRulesEngine()
                .withRule(timeRangeCheck)
                .withRule(normalStatusCheck)
                .withFacts(facts).build();

        ExecuteResult executeResult = scheduler.fire();
        System.out.println("规则执行结果:" + executeResult);

        Assertions.assertNotNull(executeResult);
        Assertions.assertEquals(ExecuteResult.State.FAIL, executeResult.getState());
    }

    @Test
    @DisplayName("规则顺序动态调整测试")
    void rulePriorityChangeTest()
    {
        Date now = new Date();
        Facts facts = new Facts();
        facts.put("beginTime", now.getTime() - one_day * 3);
        facts.put("endTime", now.getTime() + one_day * 4);
        facts.put("status", -1);

        DemoScheduler scheduler = (DemoScheduler) ruleSchedulerBuilder
                .newScheduler(DemoScheduler.class).withRulesEngine()
                .withRule(timeRangeCheck)
                .withRule(normalStatusCheck)
                .withFacts(facts).build();

        ExecuteResult executeResult = scheduler.fire();
        System.out.println("规则执行结果:" + executeResult);

        Assertions.assertNotNull(executeResult);
        Assertions.assertEquals(ExecuteResult.State.FAIL, executeResult.getState());
        Assertions.assertNotNull(facts.get("testTime"));

        // 执行顺序改为 -10
        facts = new Facts();
        facts.put("beginTime", now.getTime() - one_day * 3);
        facts.put("endTime", now.getTime() + one_day * 4);
        facts.put("status", -1);

        scheduler = (DemoScheduler) ruleSchedulerBuilder
                .newScheduler(DemoScheduler.class).withRulesEngine()
                .withRule(timeRangeCheck)
                .withRule(normalStatusCheck, -10)
                .withFacts(facts).build();

        executeResult = scheduler.fire();
        System.out.println("规则执行结果:" + executeResult);

        Assertions.assertNotNull(executeResult);
        Assertions.assertEquals(ExecuteResult.State.FAIL, executeResult.getState());
        Assertions.assertNull(facts.get("testTime"));
    }
}
