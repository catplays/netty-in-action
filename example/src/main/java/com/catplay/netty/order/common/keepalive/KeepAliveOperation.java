package com.catplay.netty.order.common.keepalive;

import com.catplay.netty.order.common.Operation;
import com.catplay.netty.order.common.OperationResult;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class KeepAliveOperation extends Operation {

    private long time ;

    public KeepAliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public OperationResult execute() {
        KeepAliveOperationResult orderResponse = new KeepAliveOperationResult(time);
        return orderResponse;
    }
}
