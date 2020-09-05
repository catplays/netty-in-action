package com.catplay.netty.order.common.auth;

import com.catplay.netty.order.common.OperationResult;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class AuthOperationResult extends OperationResult {
    private boolean authPass;

    public AuthOperationResult() {
    }

    public AuthOperationResult(boolean authPass) {
        this.authPass = authPass;
    }

    public boolean isAuthPass() {
        return authPass;
    }
}
