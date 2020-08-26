package com.v.component.rule.engine.groovy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.v.component.rule.engine.core.RuleGroovyScriptExecutor;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Condition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : DaiGD
 * @createtime :  2020年08月24日 16:42
 * @description :
 */
@Slf4j
@Component
public class RuleConditionGroovyExecutor implements RuleGroovyScriptExecutor<Condition>
{
    
    private final ApplicationContext applicationContext;

    private Map<String, Class<Condition>> nameAndClass = Maps.newConcurrentMap();

    private Map<String, String> nameAndMd5 = Maps.newConcurrentMap();

    private final GroovyScriptTemplateService groovyScriptTemplateService;

    public RuleConditionGroovyExecutor(GroovyScriptTemplateService groovyScriptTemplateService,
            ApplicationContext applicationContext)
    {
        this.groovyScriptTemplateService = groovyScriptTemplateService;
        this.applicationContext = applicationContext;
    }

    @Override
    public Condition getInstance(String name)
    {
        try
        {
            Class<Condition> aClass = nameAndClass.get(name);
            
            if(aClass == null)
            {
                throw new IllegalArgumentException(String.format("script:%s not load", name));
            }
            return aClass.newInstance();
            //return aClass;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override public void parseAndCache(String name, String script)
    {
        try
        {
            Preconditions.checkNotNull(name);
            Preconditions.checkNotNull(script);
            String scriptBuilder = groovyScriptTemplateService.getScript("RuleConditionTemplate.groovy_template");
            String scriptClassName = RuleConditionGroovyExecutor.class.getSimpleName() + "_" + name;
            String fullScript = String.format(scriptBuilder, scriptClassName, script);

            //            String oldMd5 = nameAndMd5.get(name);
            //            String newMd5 = MD5Utils.getStringMD5(fullScript);
            //            if (oldMd5 != null && oldMd5.equals(newMd5)) {
            //                return;
            //            }

            GroovyClassLoader classLoader = new GroovyClassLoader();
            Class aClass = classLoader.parseClass(fullScript);
            //
//            Script script1 = ((Script) classInstance.newInstance());
//            Binding binding = new Binding();
//
//            script1.setBinding(binding);
            
//            AutowiredAnnotationBeanPostProcessor aabpp =
//                    (AutowiredAnnotationBeanPostProcessor) applicationContext
//                            .getBean(AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME);
//            aabpp.processInjection(classInstance);

            // 2. create the bean definition
//            AbstractBeanDefinition beanDef = BeanDefinitionReaderUtils
//                    .createBeanDefinition(null, scriptClassName, classLoader);

            // 3. inject here any attributes that would normally be passed using spring XML configuration files
           // beanDef.setAttribute("testService", testService);
            //beanDef.set
            //beanDef.setDependsOn("testService");
            
            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            
            // 4. Create the bean - I'm using the class name as the bean name
           // factory.registerBeanDefinition(scriptClassName, beanDef);
//            
         //  Object bean = factory.createBean(classInstance, AUTOWIRE_BY_NAME, false);
            
            log.info("Full script:{}", fullScript);
            log.info("collection-engine load script:{} finish", name);
            nameAndClass.put(name, aClass);
            
            // nameAndMd5.put(name, newMd5);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
