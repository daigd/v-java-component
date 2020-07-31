package com.v.component.rule.engine.core;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 9:58
 * @description : 规则调度器抽象父类，提供
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AbstractRuleScheduler implements RuleScheduler
{
    protected RulesEngine rulesEngine;
    
    protected Rules rules;
    
    protected Facts facts;

    AbstractRuleScheduler buildRulesEngine(RulesEngine rulesEngine)
    {
        this.rulesEngine = rulesEngine;
        return this;
    }

    AbstractRuleScheduler buildRules(Rules rules)
    {
        this.rules = rules;
        return this;
    }

    AbstractRuleScheduler buildFacts(Facts facts)
    {
        this.facts = facts;
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 10:45
     * @description : 规则处理完毕后的返回给调用方的数据：
     * 支持自定义类型或者简单类型（Integer、Long、Float、Double、Short、String、Boolean等）（实现序列化接口）
     */
    protected ExecuteResult createExecuteResult()
    {
        ExecuteResult executeResult = new ExecuteResult();
        executeResult.setState(this.facts.get(ExecuteResult.EXECUTE_RESULT_STATUS_KEY));
        return executeResult;
    }
    
    @Override public ExecuteResult fire()
    {
        Assert.notNull(this.rulesEngine, "RulesEngine cannot be null!");
        Assert.notNull(this.rules, "Rules cannot be null!");
        Assert.isTrue(!this.rules.isEmpty(), "Rules cannot be empty!");
        Assert.notNull(this.facts, "Facts cannot be null!");
        this.rulesEngine.fire(this.rules, this.facts);
        return this.createExecuteResult();
    }
    
}
