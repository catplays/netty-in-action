package com.catplay.netty.ws;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 *  扩展 ChatServerInitializer 以添加加密
 *
 * @Author wangyong
 * @Date 2020-09-05
 */
public class SecureChatServerInitializer extends ChatServerInitializer {
    private final SslContext context;
    public SecureChatServerInitializer(ChannelGroup group, SslContext context) {
        super(group);
        this.context = context;
    }

    /**
     * 调用父类的 initChannel()方法
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(false);

        ch.pipeline().addFirst(new SslHandler(engine));
    } }