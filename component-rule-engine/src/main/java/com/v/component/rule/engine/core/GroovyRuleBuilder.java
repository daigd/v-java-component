package com.v.component.rule.engine.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.jeasy.rules.api.Rule;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : DaiGD
 * @createtime :  2020年08月25日 16:50
 * @description : groovy规则构建器:参考RuleBuilder
 */
public class GroovyRuleBuilder
{
    private String name = Rule.DEFAULT_NAME;
    private String description = Rule.DEFAULT_DESCRIPTION;
    private int priority = Rule.DEFAULT_PRIORITY;

    private final Map<String, Object> given = new HashMap<>();

    private String condition = "return true";

    private final List<String> actions = new ArrayList<>();

    private final Cache<String, Script> cache = Caffeine.newBuilder().expireAfterAccess(2, TimeUnit.HOURS).maximumSize(1000)
            .build();

    private final GroovyShell groovyShell;

    public GroovyRuleBuilder(GroovyShell groovyShell)
    {
        this.groovyShell = groovyShell;
    }

    /**
     * Set rule name.
     *
     * @param name of the rule
     * @return the builder instance
     */
    public GroovyRuleBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set rule description.
     *
     * @param description of the rule
     * @return the builder instance
     */
    public GroovyRuleBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set rule priority.
     *
     * @param priority of the rule
     * @return the builder instance
     */
    public GroovyRuleBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public GroovyRuleBuilder given(Map<String, Object> given)
    {
        this.given.putAll(given);
        return this;
    }

    /**
     * Set rule condition.
     *
     * @param condition of the rule
     * @return the builder instance
     */
    public GroovyRuleBuilder when(String condition) {
        Assert.isTrue(!StringUtils.isEmpty(condition));
        this.condition = condition;
        return this;
    }

    /**
     * Add an action to the rule.
     *
     * @param action to add
     * @return the builder instance
     */
    public GroovyRuleBuilder then(String action) {
        Assert.isTrue(!StringUtils.isEmpty(action));
        this.actions.add(action);
        return this;
    }

    public GroovyRuleBuilder then(List<String> actions) {
        Assert.isTrue(!CollectionUtils.isEmpty(actions));
        this.actions.addAll(actions);
        return this;
    }

    /**
     * Create a new {@link Rule}.
     *
     * @return a new rule instance
     */
    public Rule build()
    {
        // 将 given 数据设置成脚本参数
        if(!CollectionUtils.isEmpty(this.given))
        {
            Map<String, Object> map = this.given;
            for(String key : map.keySet())
            {
                groovyShell.setVariable(key, map.get(key));
            }
        }
        Assert.isTrue(!CollectionUtils.isEmpty(actions), "Rule actions cannot be empty!");
        GroovyRule groovyRule = new GroovyRule(name, description, priority);
        groovyShell.setVariable("log", LoggerFactory.getLogger(groovyRule.getClass()));
        // 解析脚本并缓存
        Script conditionScript = parseAndCache(this.condition);
        List<Script> actionScripts = this.actions.stream().map(this::parseAndCache).collect(Collectors.toList());

        groovyRule.setScript(conditionScript, actionScripts);
        return groovyRule;
    }

    private Script parseAndCache(String scriptText)
    {
        String key = DigestUtils.md5DigestAsHex(scriptText.getBytes()).toLowerCase();
        Script script = cache.getIfPresent(key);
        if(script != null)
        {
            return script;
        }
        script = groovyShell.parse(scriptText);
        Assert.notNull(script);
        cache.put(key, script);
        return script;
    }

}
