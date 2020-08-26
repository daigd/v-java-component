package com.v.component.rule.engine.core;

/**
 * @author : DaiGD
 * @createtime :  2020年08月24日 16:44
 * @description : 用于执行Groovy脚本
 */
public interface RuleGroovyScriptExecutor<T>
{
    /**
     * 获取脚本执行实例
     */
    T getInstance(String name);

    /**
     * 解析脚本并缓存
     */
    void parseAndCache(String name, String script);
}
