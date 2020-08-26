package com.v.component.rule.engine.core;

import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.BasicRule;

import java.util.List;

/**
 * @author : DaiGD
 * @createtime :  2020年08月25日 16:47
 * @description : 基于Groovy脚本生成的规则
 */
@Slf4j
class GroovyRule extends BasicRule
{

    private Script condition;

    private List<Script> actions;

    GroovyRule(String name, String description, int priority)
    {
        super(name, description, priority);
    }

    public void setScript(Script condition, List<Script> actions) {
        this.condition = condition;
        this.actions = actions;
    }

    @Override
    public boolean evaluate(Facts facts)
    {
        return (boolean) this.condition.run();
    }

    @Override
    public void execute(Facts facts) throws Exception {
        for (Script action : actions) {
            action.run();
        }
    }
}

