import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author wangyong
 * @Date 2020-08-11
 */
public class Test {

    public static void main(String[] args) {
        final AttributeKey<Integer> id = new AttributeKey<Integer>("ID");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .handler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx)
                                    throws Exception {
                                Integer idValue = ctx.channel().attr(id).get(); // do something with the idValue
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
                                System.out.println("Received data");
                            }
                        }
                );
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.attr(id, 123456);
        ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
    }


    public static class SslChannelInitializer extends ChannelInitializer<Channel> {
        private final SslContext context;
        private final boolean startTls;

        /**
         * @param context  传入要使用的SslContext
         * @param startTls 如果设置为 true，第一个 写入的消息将不会被加密 客户端应该设置为 true
         */
        public SslChannelInitializer(SslContext context, boolean startTls) {
            this.context = context;
            this.startTls = startTls;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            /**
             *  对于每个 SslHandler 实例， 都使用 Channel 的 ByteBuf- Allocator 从 SslContext 获 取一个新的 SSLEngine
             */
            SSLEngine engine = context.newEngine(ch.alloc());
            /**
             * 将 SslHandler 作为第一个 ChannelHandler 添加到 ChannelPipeline 中
             */
            ch.pipeline().addFirst("ssl",
                    new SslHandler(engine, startTls));
        }
    }

    public static class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel ch) throws Exception {

            /**
             *  将一个 Heart- beatHandler 添加到ChannelPipeline 中IdleStateHandler,
             */
            ChannelPipeline pipeline = ch.pipeline();
            /**
             * 如果在 60 秒之内没有接收或者发送任何的数据, 将被触发时发送一个IdleStateEvent 事件
             */
            pipeline.addLast(
                    new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
            pipeline.addLast(new HeartbeatHandler());
        }
    }

    public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {
        /**
         * 发送到远程节
         * 点的心跳消息
         */
        private static final ByteBuf HEARTBEAT_SEQUENCE =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx,
                                       Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                /**
                 *  发送心跳消息，并在发送失败时关闭该连接
                 */
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(
                                ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                /**
                 * 不是 IdleStateEvent 事件，所以将它传递 给下一个 Channel- InboundHandler
                 */
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
