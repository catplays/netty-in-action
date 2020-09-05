package com.catplay.netty.order.server;

import com.catplay.netty.order.server.codec.OrderFrameDecoder;
import com.catplay.netty.order.server.codec.OrderFrameEncoder;
import com.catplay.netty.order.server.codec.OrderProtocolDecoder;
import com.catplay.netty.order.server.codec.OrderProtocolEncoder;
import com.catplay.netty.order.server.handler.OrderServerProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new OrderFrameDecoder());
                            pipeline.addLast(new OrderProtocolDecoder());
                            pipeline.addLast(new OrderFrameEncoder());
                            pipeline.addLast(new OrderProtocolEncoder());

                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new OrderServerProcessHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }





    }
}
