package com.catplay.netty.order.client.codec;

import com.catplay.netty.order.common.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 二级编码器，用于请求对象转换为二进制
 * @Author wangyong
 * @Date 2020-04-01
 */
public class OrderProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        msg.encode(byteBuf);
        out.add(byteBuf);
    }
}
