package com.v.component.rule.engine.rules;

import com.v.component.rule.engine.core.AbstractPriorityRule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.springframework.stereotype.Component;

/**
 * @author : DaiGD
 * @createtime :  2020年07月30日 15:20
 * @description : 状态检查,只有正常状态(status=1)才能通过
 */
@Component
@Rule(name = "NormalStatusCheck", description = "正常状态检查", priority = 10)
@Slf4j
@Data
public class NormalStatusCheck extends AbstractPriorityRule
{
    private int priority = 10;
    
    @Condition
    public boolean when(Facts facts)
    {
        Integer status = facts.get("status");

        if(status == null)
        {
            return false;
        }
        if(status == 1)
        {
            return true;
        }
        log.info("状态检查不通过");
        return false;
    }

    @Action
    public void then()
    {
        log.info("状态检查通过");
    }

    // 方法上定义执行顺序会覆盖类上 @Rule 上定义
    @Priority
    public int getPriority()
    {
        log.debug("规则:{},执行顺序 priority:{}", this.getClass().getSimpleName(), this.priority);
        return this.priority;
    }
    
}
