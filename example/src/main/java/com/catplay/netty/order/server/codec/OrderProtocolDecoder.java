package com.catplay.netty.order.server.codec;

import com.catplay.netty.order.common.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 二级解码器，用于将二级制转为对象
 * @Author wangyong
 * @Date 2020-04-01
 */
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.decode(msg);
        out.add(requestMessage);
    }
}
