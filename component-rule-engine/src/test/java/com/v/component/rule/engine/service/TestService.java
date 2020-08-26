package com.v.component.rule.engine.service;

import org.springframework.stereotype.Service;

/**
 * @author : DaiGD
 * @createtime :  2020年08月25日 16:18
 * @description :
 */
@Service
public class TestService
{
    public String hi()
    {
        return "Hi Boy!";
    }

    public String hi(String name)
    {
        return "Hi:" + name;
    }
}
