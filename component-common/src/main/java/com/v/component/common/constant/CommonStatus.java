package com.v.component.common.constant;

import java.util.Arrays;

/**
 * @author : DaiGD
 * @createtime :  2020年08月06日 11:21
 * @description : 通用状态
 */
public enum CommonStatus
{
    UNKNOWN((byte) -100, "未知状态"),
    NORMAL((byte) 1, "正常"),
    FORBIDDEN((byte) -1, "禁止"),
    ;

    private Byte status;

    private String desc;

    /**
     * @param status 状态值
     * @param desc   描述
     */
    CommonStatus(Byte status, String desc)
    {
        this.status = status;
        this.desc = desc;
    }

    public Byte getStatus()
    {
        return status;
    }

    public String getDesc()
    {
        return desc;
    }

    public static CommonStatus getByStatus(Byte status)
    {
        return Arrays.stream(CommonStatus.values()).filter(r -> r.status.equals(status)).findFirst().orElse(UNKNOWN);
    }
}
