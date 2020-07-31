package com.v.component.rule.engine.core;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 9:52
 * @description : 规则调度器，负责调度规则执行
 */
public interface RuleScheduler
{
    /**
     * 执行规则,返回实现了序列化接口的对象 
     */
    ExecuteResult fire();
}
