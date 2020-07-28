package com.v.component.common.constant;

import java.util.Arrays;

/**
 * @author : DaiGD
 * @createtime :  2020年07月28日 10:50
 * @description : 业务响应码
 */
public enum BizResponseCode
{
    FAIL(-100, "处理失败"),
    SUCCESS(100, "处理成功"),
    ;

    private Integer code;

    private String desc;

    /**
     * @param code 响应码
     * @param desc 响应描述
     */
    BizResponseCode(Integer code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getDesc()
    {
        return desc;
    }

    public static BizResponseCode getByCode(Integer code)
    {
        return Arrays.stream(BizResponseCode.values()).filter(r -> r.code.equals(code)).findFirst().orElse(FAIL);
    }
}
