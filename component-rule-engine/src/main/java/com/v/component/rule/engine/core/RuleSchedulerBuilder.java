package com.v.component.rule.engine.core;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.*;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 10:50
 * @description : 规则调度器创建工具
 */
@Slf4j
public class RuleSchedulerBuilder
{
    private static final ThreadLocal<AbstractRuleScheduler> RULE_SCHEDULER_THREAD_LOCAL = new ThreadLocal<>();
    
    private static final ThreadLocal<RulesEngine> RULES_ENGINE_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<Rules> RULES_THREAD_LOCAL = ThreadLocal.withInitial(Rules::new);

    private static final ThreadLocal<Facts> FACTS_THREAD_LOCAL = ThreadLocal.withInitial(Facts::new);

    /**
     * 对规则引擎进行缓存
     */
    private static final ConcurrentHashMap<Byte, RulesEngine> RULES_ENGINE_MAP = new ConcurrentHashMap<>();
    
    // 先执行的规则有一个执行了，后续规则不再执行
    private static final Byte SKIP_ON_FIRST_APPLIED_RULE = 1;

    // 只要第一个规则出现
    private static final Byte SKIP_ON_FIRST_FAILED_RULE = 1 << 2;

    // 只要第一个规则不触发,后续规则不再执行
    private static final Byte SKIP_ON_FIRST_NON_TRIGGERED_RULE = 1 << 3;
    
    private RuleEngineContext context;
    
    private RuleBuilder builder;
    
    public RuleSchedulerBuilder(RuleEngineContext context, RuleBuilder builder)
    {
        Assert.notNull(context, "RuleEngineContext cannot be null!");
        Assert.notNull(builder, "RuleBuilder cannot be null!");
        this.context = context;
        this.builder = builder;
    }

    public <T extends AbstractRuleScheduler> RuleSchedulerBuilder newScheduler(Class<T> clazz)
    {
        log.debug(" thread[{}] newScheduler begin", Thread.currentThread().getName());
        RULE_SCHEDULER_THREAD_LOCAL.set(context.getScheduler(clazz));
        log.debug(" thread[{}] newScheduler end", Thread.currentThread().getName());
        return this;
    }

    public RuleScheduler build()
    {
        log.debug("RuleScheduler build begin");
        AbstractRuleScheduler scheduler = RULE_SCHEDULER_THREAD_LOCAL.get();
        Assert.notNull(scheduler, "RuleScheduler cannot be null!");

        // 对规则相关参数进行非空判断
        RulesEngine rulesEngine = RULES_ENGINE_THREAD_LOCAL.get();
        Assert.notNull(rulesEngine, "RulesEngine cannot be null!");
        Rules rules = RULES_THREAD_LOCAL.get();
        Assert.notNull(rules, "Rules cannot be null!");
        for(Rule rule : rules)
        {
            log.info("RuleScheduler build rule:{}", rule);
        }
        Facts facts = FACTS_THREAD_LOCAL.get();
        Assert.notNull(facts, "Facts cannot be null!");
        log.info("RuleScheduler build facts:{}", facts);
        
        scheduler.buildRulesEngine(rulesEngine);
        scheduler.buildRules(rules);
        scheduler.buildFacts(facts);
        
        this.clear();
        
        log.debug("RuleScheduler build end");
        return scheduler;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 11:20
     * @description : 默认的规则引擎：只要第一个规则不触发,后续规则不再执行
     * @param 
     * @return
     */
    public RuleSchedulerBuilder withRulesEngine()
    {
        log.debug(" thread[{}] withRulesEngine begin", Thread.currentThread().getName());
        this.withRulesEngine(false, false, true);
        log.debug(" thread[{}] withRulesEngine end", Thread.currentThread().getName());
        return this;
    }

    public RuleSchedulerBuilder withRulesEngine(final boolean skipOnFirstAppliedRule, final boolean skipOnFirstFailedRule,
            final boolean skipOnFirstNonTriggeredRule)
    {
        if(!skipOnFirstAppliedRule && !skipOnFirstFailedRule && skipOnFirstNonTriggeredRule)
        {
            RulesEngine rulesEngine = RULES_ENGINE_MAP.getOrDefault(SKIP_ON_FIRST_NON_TRIGGERED_RULE,
                    new DefaultRulesEngine(new RulesEngineParameters().skipOnFirstNonTriggeredRule(true)));
            RuleListener ruleListener = context.getRuleListener();
            if(ruleListener != null)
            {
                log.info("Find RuleListener:{}", ruleListener.getClass().getSimpleName());
                ((DefaultRulesEngine) (rulesEngine)).registerRuleListener(ruleListener);
            }
            RULES_ENGINE_THREAD_LOCAL.set(rulesEngine);
        }
        else
        {
            throw new RuntimeException("其它参数配置的规则引擎未初始化");
        }
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 13:33
     * @description : 添加指定规则实例
     * @param rule 具体的规则实例
     */
    public RuleSchedulerBuilder withRule(final Object rule)
    {
        Assert.notNull(rule, "Rule cannot be null!");
        this.addRule2ThreadLocal(rule);
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 13:33
     * @description : 添加指定规则实例，指定执行顺序
     * @param rule 具体的规则实例
     * @param priority 执行顺序
     */
    public RuleSchedulerBuilder withRule(final AbstractPriorityRule rule,final int priority)
    {
        Assert.notNull(rule, "Rule cannot be null!");
        rule.setPriority(priority);
        this.addRule2ThreadLocal(rule);
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 11:50
     * @description : 创建规则
     * @param condition 规则条件
     * @param actions 规则执行的动作
     * @return
     */
    public RuleSchedulerBuilder withRule(final Condition condition, final List<Action> actions)
    {
        Assert.notNull(condition, "Rule condition cannot be null!");
        Assert.isTrue(!CollectionUtils.isEmpty(actions), "Rule actions cannot be empty!");

        log.debug(" thread[{}] withRule begin", Thread.currentThread().getName());
        builder.priority(Integer.MIN_VALUE).when(condition);
        
        for(Action action : actions)
        {
            builder.then(action);
        }
        Rule rule = builder.build();
        log.debug(" thread[{}] withRule Rule is:{}", Thread.currentThread().getName(), rule);
        this.addRule2ThreadLocal(rule);
        log.debug(" thread[{}] withRule end", Thread.currentThread().getName());
        return this;
    }
    
    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 11:52
     * @description : 创建规则
     * @param condition 规则条件
     * @param actions 规则执行的动作
     * @param priority 规则顺序
     * @return
     */
    public RuleSchedulerBuilder withRule(final Condition condition, final List<Action> actions, final int priority)
    {
        Assert.notNull(condition, "Rule condition cannot be null!");
        Assert.isTrue(!CollectionUtils.isEmpty(actions), "Rule actions cannot be empty!");

        log.debug(" thread[{}] withRule begin", Thread.currentThread().getName());
        builder.priority(priority).when(condition);
        for(Action action : actions)
        {
            builder.then(action);
        }
        Rule rule = builder.build();
        log.debug(" thread[{}] withRule Rule is:{}", Thread.currentThread().getName(), rule);
        this.addRule2ThreadLocal(rule);
        log.debug(" thread[{}] withRule end", Thread.currentThread().getName());
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 11:50
     * @description : 创建规则，具体的创建逻辑委托给 easy-rules 的 RuleBuilder 执行
     * @param name 规则名称
     * @param description 规则描述
     * @param condition 规则条件
     * @param actions 规则执行的动作
     * @param priority 规则顺序
     * @return
     */
    public RuleSchedulerBuilder withRule(final String name, final String description, final Condition condition,
            final List<Action> actions, final int priority)
    {
        Assert.notNull(condition, "Rule condition cannot be null!");
        Assert.isTrue(!CollectionUtils.isEmpty(actions), "Rule actions cannot be empty!");

        log.debug(" thread[{}] withRule begin", Thread.currentThread().getName());
        if(!StringUtils.isEmpty(name))
        {
            builder.name(name);
        }
        if(!StringUtils.isEmpty(description))
        {
            builder.description(description);
        }
        builder.priority(priority).when(condition);
        for(Action action : actions)
        {
            builder.then(action);
        }
        Rule rule = builder.build();
        log.debug(" thread[{}] withRule Rule is:{}", Thread.currentThread().getName(), rule);
        this.addRule2ThreadLocal(rule);
        log.debug(" thread[{}] withRule end", Thread.currentThread().getName());
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 13:39
     * @description : 添加规则事实集合
     * @param 
     * @return
     */
    public RuleSchedulerBuilder withFacts(final Facts facts)
    {
        Assert.notNull(facts, "Facts cannot be null!");
        log.debug(" thread[{}] withFacts begin", Thread.currentThread().getName());
        FACTS_THREAD_LOCAL.set(facts);
        log.debug(" thread[{}] withFacts end", Thread.currentThread().getName());
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 13:47
     * @description : 添加一条规则事实
     * @param name 事实名称 
     * @param value 事实实例数据
     */
    public RuleSchedulerBuilder withFact(final String name, final Object value)
    {
        Assert.isTrue(!StringUtils.isEmpty(name), "Fact name cannot be blank!");
        Assert.notNull(value, "Fact value cannot be null!");

        log.debug(" thread[{}] withFacts begin,Fact name:{},value:{}", Thread.currentThread().getName(), name, value);
        this.addFact2ThreadLocal(new Fact(name, value));
        log.debug(" thread[{}] withFacts end", Thread.currentThread().getName());
        return this;
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 11:45
     * @description : 将规则添加到线程变量中
     */
    private void addRule2ThreadLocal(Object rule)
    {
        Rules rules = RULES_THREAD_LOCAL.get();
        rules.register(rule);
    }

    /**
     * @author : daiguodong@viomi.com.cn
     * @createtime :  2020年07月31日 13:44
     * @description : 添加规则事实到线程变量中
     */
    private void addFact2ThreadLocal(Fact fact)
    {
        Facts facts = FACTS_THREAD_LOCAL.get();
        facts.add(fact);
    }

    private void clear()
    {
        RULE_SCHEDULER_THREAD_LOCAL.remove();
        RULES_ENGINE_THREAD_LOCAL.remove();
        RULES_THREAD_LOCAL.remove();
        FACTS_THREAD_LOCAL.remove();
    }
}
