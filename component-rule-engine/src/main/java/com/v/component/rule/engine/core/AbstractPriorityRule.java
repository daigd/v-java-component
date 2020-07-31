package com.v.component.rule.engine.core;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 17:18
 * @description : 规则父类，支持动态修改执行顺序
 */
public abstract class AbstractPriorityRule
{
    protected int priority = Integer.MIN_VALUE;

    // 方法上定义执行顺序会覆盖类上 @Rule 上定义,子类上需要在方法上加上@Priority注解
    public abstract int getPriority();
    
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
