package com.catplay.netty.order.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 一级解码器，用于解决粘包问题
 * @Author wangyong
 * @Date 2020-04-01
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
