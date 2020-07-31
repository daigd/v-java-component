package com.v.component.rule.engine.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : DaiGD
 * @createtime :  2020年07月31日 15:39
 * @description : 执行结果
 */
@Data
public class ExecuteResult implements Serializable
{
    public static final String EXECUTE_RESULT_STATUS_KEY = "executeResultStatus";
    
    public enum State
    {
        SUCCESS,
        FAIL
    }

    private State state;

    private String message;
}
