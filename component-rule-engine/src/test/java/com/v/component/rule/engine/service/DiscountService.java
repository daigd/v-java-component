package com.v.component.rule.engine.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author : DaiGD
 * @createtime :  2020年08月25日 17:33
 * @description : 数据查询服务
 */
@Service
public class DiscountService
{
    private Random random = new Random();

    // 模拟数据库查询服务
    public Long queryDiscountByUserId(Long userId)
    {
        return (long) random.nextInt(1000);
    }
}
