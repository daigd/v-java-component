package com.v.component.rule.engine.groovy

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true)
class BizRuleTemplate {
    // 规则名称
    String name
    // 规则描述
    String description
    // 规则执行顺序
    int priority
    // 给定的事实数据
    def given = [:]
    // 规则条件:返回布尔值
    String when
    // 执行的结果
    def then = []

    // 等效于 when then,when 条件恒为true
    def expect = []

}