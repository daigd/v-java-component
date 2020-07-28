package com.v.component.common.exception;

import com.v.component.common.constant.BizResponseCode;

/**
 * @author : DaiGD
 * @createtime :  2020年07月28日 10:58
 * @description : 业务异常父类
 */
public class AbstractBizException extends RuntimeException
{
    // 业务响应码
    private Integer bizResponseCode = null;

    public AbstractBizException()
    {
        super();
    }

    public AbstractBizException(String message)
    {
        super(message);
    }

    public AbstractBizException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public AbstractBizException(String message, Integer bizResponseCode)
    {
        super(message);
        this.bizResponseCode = bizResponseCode;
    }

    public AbstractBizException(String message, Throwable cause, Integer bizResponseCode)
    {
        super(message, cause);
        this.bizResponseCode = bizResponseCode;
    }

    public void setBizResponseCode(Integer bizResponseCode)
    {
        this.bizResponseCode = bizResponseCode;
    }

    public int getResponseCode()
    {
        if(null == bizResponseCode)
        {
            bizResponseCode = BizResponseCode.FAIL.getCode();
        }
        return bizResponseCode;
    }
}
