package com.v.component.rule.engine.groovy

import com.v.component.rule.engine.core.GroovyRuleBuilder
import org.codehaus.groovy.control.CompilerConfiguration
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.RulesEngine
import org.jeasy.rules.core.DefaultRulesEngine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.yaml.snakeyaml.Yaml
import spock.lang.Specification

/**
 * @author : DaiGD
 * @createtime :  2020年08月25日 14:19
 * @description : 规则模板测试
 */
@SpringBootTest
class BizRuleTemplateTest extends Specification {
    @Autowired
    Binding binding

    def "解析yml字符,转成规则模板"() {
        given:
        def rule = """
        name: 测试规则
        description: 这是规则描述
        priority: 1
        given:
            userId: 212
            userName: David
        when:
            person.age > 18
        then:
            - testService.hi()
              println(userId)    
            - println(userName)    
        """
        when:
        Yaml yaml = new Yaml()
        BizRuleTemplate template = yaml.load(rule)
        then:
        template.name == "测试规则"
        template.description == "这是规则描述"
        template.priority == 1
        template.given.get("userId") == 212
        template.given.get("userName") == "David"
        template.when == "person.age > 18"
        template.then.size() == 2
        template.then.each { it -> println it }
        println "template:\n"
        println template
    }

    def "读取yml文件,转成业务规则"() {
        given:
        Yaml yaml = new Yaml()
        def path = "src/test/resources/biz-rule-template.yml"
        

        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(this.getClass().getClassLoader())
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.setSourceEncoding("utf-8")
        GroovyShell groovyShell = new GroovyShell(groovyClassLoader, binding, compilerConfiguration)

        RulesEngine rulesEngine = new DefaultRulesEngine()
        GroovyRuleBuilder builder = new GroovyRuleBuilder(groovyShell)
        
        when:
        BizRuleTemplate template = yaml.load((path as File).text)

        def rule = builder.name(template.name).description(template.description)
                .priority(template.priority)
                .given(template.given as Map<String, Object>)
                .when(template.when)
                .then(template.then.get(0) as String)
                .then(template.then.get(1) as String)
                .build()
        Rules rules = new Rules(rule)
        Facts facts = new Facts()
        
        rulesEngine.fire(rules, facts)
        
        then:
        assert null  != rule
        template.name == "测试规则"
        template.description == "这是规则描述"
        template.priority == 1
        template.given.get("userId") == 212
        template.given.get("userName") == "David"
        template.then.size() == 2
    }

    def "验证expect规则"() {
        given: "规则yaml文件"
        Yaml yaml = new Yaml()
        def path = "src/test/resources/expect-template.yml"

        GroovyShell groovyShell = new GroovyShell(binding)
        RulesEngine rulesEngine = new DefaultRulesEngine()

        GroovyRuleBuilder builder = new GroovyRuleBuilder(groovyShell)

        when:

        BizRuleTemplate template = yaml.load((path as File).text)

        def rule = builder.name(template.name).description(template.description)
                .priority(template.priority)
                .given(template.given as Map<String, Object>)
                .then(template.then ? template.then : template.expect)
                .build()
        Rules rules = new Rules(rule)
        Facts facts = new Facts()
        rulesEngine.fire(rules, facts)
        then:
        assert null  != rule
        template.name == "测试规则"
        template.description == "这是规则描述"
        template.priority == 1
        template.given.get("userId") == 212
        template.given.get("userName") == "David"
        template.then.size() == 0
        template.expect.size() == 2
    }
}
