package com.catplay.netty.ws;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @Author wangyong
 * @Date 2020-09-05
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(
                    "Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             FullHttpRequest request) throws Exception {

        /**
         *  如果请求了 WebSocket 协议升级，则增加引用 计数 (调用 retain() 方法)，并将它传递给下一个 ChannelInboundHandler
         *  扩展 SimpleChannel -InboundHandler 以处理 FullHttpRequest 消息
         *  之所以需要调用retain()方法，是因为调用channelRead() 方法完成之后，它将调用 FullHttpRequest 对象上的 release()方法以释放它的资源
         */
        if (wsUri.equalsIgnoreCase(request.getUri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            if (HttpHeaders.is100ContinueExpected(request)) {
                // 处理 100 Continue 请求以符合 HTTP 1.1 规范
                send100Continue(ctx);
            }

            //读取 index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(
                    request.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(
                    HttpHeaders.Names.CONTENT_TYPE,
                    "text/plain; charset=UTF-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(request);
            if (keepAlive) {

                /**
                 *   如果请求了 keep -alive，则添加所需要的 HTTP 头信息
                 *
                 */
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            //  将 HttpResponse 写到客户端
            ctx.write(response);
            /**
             * 如果不需要加密和压缩，那么可以通过将 index.html 的内容存储到 DefaultFile- Region 中来达到最佳效率。
             * 这将会利用零拷贝特性来进行内容的传输。为此，你可以检查一下， 是否有 SslHandler 存在于在 ChannelPipeline 中
             * 否则，你可以使用 ChunkedNioFile
             */
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            //写 LastHttpContent 并冲刷至客户端
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                // 如果没有请求 keep-alive， 则在写操作完成后关闭 Channel
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }

    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}