package com.v.component.rule.engine.core;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 15:45
 * @description : 默认执行结果监听器，目前实现逻辑：只有全部规则执行成功，执行结果才成功
 */
@Slf4j
public class DefaultExecuteResultListener implements RuleListener
{
    @Override 
    public boolean beforeEvaluate(Rule rule, Facts facts)
    {
        facts.put(ExecuteResult.EXECUTE_RESULT_STATUS_KEY, ExecuteResult.State.FAIL);
        log.debug("RuleListener beforeEvaluate set executeResult = fail");
        return true;
    }
    
    @Override public void onSuccess(Rule rule, Facts facts)
    {
        facts.put(ExecuteResult.EXECUTE_RESULT_STATUS_KEY, ExecuteResult.State.SUCCESS);
        log.debug("RuleListener beforeEvaluate set executeResult = success");
    }
}
