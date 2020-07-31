package com.v.component.rule.engine.rules;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : DaiGD
 * @createtime :  2020年07月30日 15:12
 * @description : 时间范围检查
 */
@Component
@Rule(name = "TimeRangeCheck", description = "时间范围检查", priority = 0)
@Slf4j
@Data
public class TimeRangeCheck
{
    @Condition
    public boolean when(Facts facts)
    {
        Long beginTime = facts.get("beginTime");
        Long endTime = facts.get("endTime");

        if(beginTime == null || endTime == null)
        {
            return false;
        }
        long now = new Date().getTime();
        if(now > beginTime && now < endTime)
        {
            return true;
        }
        log.info("时间范围检查不通过");
        return false;
    }

    @Action
    public void then(Facts facts)
    {
        facts.put("testTime", "testTime");
        log.info("时间范围检查通过");
    }
}
