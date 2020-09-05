package com.catplay.netty.order.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 一级解码器，用于解决粘包问题
 * @Author wangyong
 * @Date 2020-04-01
 */
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super(2);
    }
}
