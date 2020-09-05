package com.catplay.netty.order.common;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public abstract class Operation extends MessageBody{

    public abstract OperationResult execute();
}
