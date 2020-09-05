package com.catplay.netty.order.client.handler.dispatcher;

import com.catplay.netty.order.common.OperationResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 点单future的处理中心
 * @Author wangyong
 * @Date 2020-04-02
 */
public class RequestPendingCenter {
    private Map<Long, OperationResultFuture> map = new ConcurrentHashMap<>();

    public void add(Long streamId, OperationResultFuture future){
        this.map.put(streamId, future);
    }

    public void set(Long streamId, OperationResult operationResult){
        OperationResultFuture operationResultFuture = this.map.get(streamId);
        if (operationResultFuture != null) {
            operationResultFuture.setSuccess(operationResult);
            this.map.remove(streamId);
        }
    }

}
