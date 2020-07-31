package com.v.component.rule.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : DaiGD
 * @createtime :  2020年07月24日 17:05
 * @description :
 */
@SpringBootApplication
public class RuleEngineApplication
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(RuleEngineApplication.class, args);
        LOGGER.info("==========启动完成=============");
    }
}
