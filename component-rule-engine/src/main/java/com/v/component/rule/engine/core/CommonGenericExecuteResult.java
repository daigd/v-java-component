package com.v.component.rule.engine.core;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 15:41
 * @description : 在执行结果中返回指定数据
 */
public class CommonGenericExecuteResult<T> extends ExecuteResult
{
    protected T result;

    public CommonGenericExecuteResult()
    {
        this.setState(State.SUCCESS);
    }
    
    public CommonGenericExecuteResult(T result)
    {
        this.result = result;
    }

    public T getResult()
    {
        return result;
    }

    public void setResult(T result)
    {
        this.result = result;
    }
}
