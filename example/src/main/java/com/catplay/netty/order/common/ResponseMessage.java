package com.catplay.netty.order.common;

/**
 * 服务端响应体
 * @Author wangyong
 * @Date 2020-04-01
 */
public class ResponseMessage extends Message<OperationResult>{
    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationResultClazz();
    }

}
