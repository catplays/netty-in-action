package com.catplay.netty.order.server.handler;

import com.catplay.netty.order.common.Operation;
import com.catplay.netty.order.common.OperationResult;
import com.catplay.netty.order.common.RequestMessage;
import com.catplay.netty.order.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 订单处理器
 * @Author wangyong
 * @Date 2020-04-01
 */
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);
        ctx.writeAndFlush(responseMessage);
    }
}
