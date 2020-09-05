package com.catplay.netty.order.common;

/**
 * 客户端请求体
 * @Author wangyong
 * @Date 2020-04-01
 */
public class RequestMessage extends Message<Operation>{
    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }

    public RequestMessage(){}

    public RequestMessage(Long streamId, Operation operation){
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setStreamId(streamId);
        messageHeader.setOpCode(OperationType.fromOperation(operation).getOpCode());
        this.setMessageHeader(messageHeader);
        this.setMessageBody(operation);
    }
}