package com.catplay.netty.order.common.auth;

import com.catplay.netty.order.common.Operation;
import com.catplay.netty.order.common.OperationResult;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class AuthOperation extends Operation {
    private String username;
    private String password;

    @Override
    public OperationResult execute() {
        if ("admin".equalsIgnoreCase(username)) {
            return new AuthOperationResult(true);
        }
        return new AuthOperationResult(false);
    }
}
