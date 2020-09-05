package com.catplay.netty.order.common.order;

import com.catplay.netty.order.common.OperationResult;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class OrderOperationResult extends OperationResult {

    private final int tableId;
    private final String dish;
    private final boolean complete;

    public OrderOperationResult(int tableId, String dish, boolean complete) {
        this.tableId = tableId;
        this.dish = dish;
        this.complete = complete;
    }
}
