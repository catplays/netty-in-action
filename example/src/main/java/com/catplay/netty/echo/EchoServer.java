package com.catplay.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @Author wangyong
 * @Date 2020-03-31
 */
public class EchoServer {

    public static int PORT = 8080;

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        EchoServerHandler serverHandler = new EchoServerHandler();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class) // 指定使用nio传输channel
                    .localAddress(new InetSocketAddress(PORT)) // 使用指定的端口使用套接字地址
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 两种设置keepAlive的方式
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(NioChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 当一个新的连接被接受时。 一个新的子channel将会被创建，而ChannelInitializer 会把你的EchoServerHandler 添加到channel的pipeLine中
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(serverHandler);
                        }
                    });
            // 异步地绑定服务器，调用sync方法阻塞等待，知道绑定完成
            ChannelFuture future = bootstrap.bind(PORT).sync();
            // 获取channel的CloseFuture, 并阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup 释放所有资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
