package com.v.component.rule.engine.config;

import com.v.component.rule.engine.core.DefaultExecuteResultListener;
import com.v.component.rule.engine.core.DefaultRuleScheduler;
import com.v.component.rule.engine.core.RuleEngineContext;
import com.v.component.rule.engine.core.RuleSchedulerBuilder;
import groovy.lang.Binding;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.core.RuleBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author : DaiGd
 * @createtime :  2020年01月08日 19:12
 * @description : 规则引擎配置类
 */
@Configuration
public class RuleEngineAutoConfiguration
{

    private final ApplicationContext applicationContext;

    public RuleEngineAutoConfiguration(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

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

    @Bean
    public DefaultRuleScheduler defaultRuleScheduler()
    {
        return new DefaultRuleScheduler();
    }

    @Bean
    public Binding groovyBinding()
    {
        Map<String, Object> beanMap = applicationContext.getBeansOfType(Object.class);
        // 遍历设置所有bean,可以根据需求在循环中对bean做过滤
        // 如果不需要对bean做过滤，直接用beanMap构造Binding对象即可
        //        for (String beanName : beanMap.keySet()) {
        //            groovyBinding.setVariable(beanName, beanMap.get(beanName));
        //        }
        return new Binding(beanMap);
    }
}
