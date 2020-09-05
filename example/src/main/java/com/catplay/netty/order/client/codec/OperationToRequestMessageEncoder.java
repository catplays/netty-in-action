package com.catplay.netty.order.client.codec;

import com.catplay.netty.order.common.Operation;
import com.catplay.netty.order.common.RequestMessage;
import com.catplay.netty.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 请求的时候加上消息号
 * @Author wangyong
 * @Date 2020-04-02
 */
public class OperationToRequestMessageEncoder extends MessageToMessageDecoder<Operation> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Operation operation, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), operation);
        out.add(requestMessage);
    }
}
