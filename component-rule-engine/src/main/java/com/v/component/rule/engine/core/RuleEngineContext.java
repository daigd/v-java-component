package com.v.component.rule.engine.core;

import org.jeasy.rules.api.RuleListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 10:10
 * @description :
 */
public class RuleEngineContext implements ApplicationContextAware
{
    private static ApplicationContext context;

    public <T extends RuleScheduler> T getScheduler(Class<T> clazz)
    {
        return context.getBean(clazz);
    }

    public RuleListener getRuleListener()
    {
        return context.getBean(RuleListener.class);
    }
    
    @Override 
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        context = applicationContext;
    }
}
