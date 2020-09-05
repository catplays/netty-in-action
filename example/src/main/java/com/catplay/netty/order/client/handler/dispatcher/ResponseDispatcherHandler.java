package com.catplay.netty.order.client.handler.dispatcher;

import com.catplay.netty.order.common.OperationResult;
import com.catplay.netty.order.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author wangyong
 * @Date 2020-04-02
 */
public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private RequestPendingCenter requestPendingCenter;

    public ResponseDispatcherHandler(RequestPendingCenter requestPendingCenter) {
        this.requestPendingCenter = requestPendingCenter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage responseMessage) throws Exception {
        OperationResult operationResult = responseMessage.getMessageBody();
        long streamId = responseMessage.getMessageHeader().getStreamId();
        requestPendingCenter.set(streamId, operationResult);
    }
}
