package com.catplay.netty.order.common.keepalive;

import com.catplay.netty.order.common.OperationResult;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class KeepAliveOperationResult extends OperationResult {
    private long time;

    public KeepAliveOperationResult(long time) {
        this.time = time;
    }
}
