package com.catplay.netty.order.common;

import com.catplay.netty.order.common.auth.AuthOperation;
import com.catplay.netty.order.common.auth.AuthOperationResult;
import com.catplay.netty.order.common.keepalive.KeepAliveOperation;
import com.catplay.netty.order.common.keepalive.KeepAliveOperationResult;
import com.catplay.netty.order.common.order.OrderOperation;
import com.catplay.netty.order.common.order.OrderOperationResult;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * 操作类型
 * @Author wangyong
 * @Date 2020-04-01
 */
public enum OperationType {

    AUTH(1, AuthOperation.class, AuthOperationResult.class),
    KEEPALIVE(2, KeepAliveOperation.class, KeepAliveOperationResult.class),
    ORDER(3, OrderOperation.class, OrderOperationResult.class);

    ;


    private int opCode;
    private Class<? extends Operation> operationClazz;
    private Class<? extends OperationResult> operationResultClazz;

    OperationType(int opCode, Class<? extends Operation> operationClazz, Class<? extends OperationResult> operationResultClazz) {
        this.opCode = opCode;
        this.operationClazz = operationClazz;
        this.operationResultClazz = operationResultClazz;
    }

    public static OperationType fromOperation(Operation operation) {
        return getOperationType(requestOp -> requestOp.operationClazz == operation.getClass());
    }

    public int getOpCode() {
        return opCode;
    }

    public Class<? extends Operation> getOperationClazz() {
        return operationClazz;
    }

    public Class<? extends OperationResult> getOperationResultClazz() {
        return operationResultClazz;
    }

    public static OperationType fromOpCode(int opCode) {
        return getOperationType(requestType -> requestType.opCode == opCode);
    }

    private static OperationType getOperationType(Predicate<OperationType> predicate) {
        return Arrays.stream(values()).
                filter( operationType -> {
            return predicate.test(operationType);
        }).findFirst().get();
    }
}
