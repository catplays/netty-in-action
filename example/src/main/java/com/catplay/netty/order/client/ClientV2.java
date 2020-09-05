package com.catplay.netty.order.client;

import com.catplay.netty.order.client.codec.*;
import com.catplay.netty.order.client.handler.dispatcher.OperationResultFuture;
import com.catplay.netty.order.client.handler.dispatcher.RequestPendingCenter;
import com.catplay.netty.order.client.handler.dispatcher.ResponseDispatcherHandler;
import com.catplay.netty.order.common.Operation;
import com.catplay.netty.order.common.OperationResult;
import com.catplay.netty.order.common.RequestMessage;
import com.catplay.netty.order.common.order.OrderOperation;
import com.catplay.netty.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * @Author wangyong
 * @Date 2020-04-02
 */
public class ClientV2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        RequestPendingCenter requestPendingCenter = new RequestPendingCenter();
        try{
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new OrderFrameDecoder());
                        pipeline.addLast(new OrderFrameEncoder());

                        pipeline.addLast(new OrderProtocolEncoder());
                        pipeline.addLast(new OrderProtocolDecoder());

                        pipeline.addLast(new ResponseDispatcherHandler(requestPendingCenter));

                        pipeline.addLast(new OperationToRequestMessageEncoder());

                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                    }
                });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080);

            channelFuture.sync();

            long streamId = IdUtil.nextId();

            RequestMessage requestMessage = new RequestMessage(
                    streamId, new OrderOperation(1002, "banana"));

            OperationResultFuture operationResultFuture = new OperationResultFuture();

            requestPendingCenter.add(streamId, operationResultFuture);

            channelFuture.channel().writeAndFlush(requestMessage);

            OperationResult operationResult = operationResultFuture.get();

            System.out.println(operationResult);

            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
