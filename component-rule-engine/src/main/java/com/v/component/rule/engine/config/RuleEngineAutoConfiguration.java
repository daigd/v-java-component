package com.v.component.rule.engine.config;

import com.v.component.rule.engine.core.DefaultExecuteResultListener;
import com.v.component.rule.engine.core.RuleEngineContext;
import com.v.component.rule.engine.core.RuleSchedulerBuilder;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.core.RuleBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : DaiGd
 * @createtime :  2020年01月08日 19:12
 * @description : 规则引擎配置类
 */
@Configuration
public class RuleEngineAutoConfiguration
{

    @Bean
    public RuleBuilder ruleBuilder()
    {
        return new RuleBuilder();
    }

    @Bean
    public RuleEngineContext ruleEngineContext()
    {
        return new RuleEngineContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public RuleListener defaultExecuteResultListener()
    {
        return new DefaultExecuteResultListener();
    }

    @Bean
    public RuleSchedulerBuilder ruleSchedulerBuilder(RuleEngineContext context, RuleBuilder builder)
    {
        return new RuleSchedulerBuilder(context, builder);
    }
    
}
