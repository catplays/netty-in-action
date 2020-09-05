package com.catplay.mqtt2.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NettyUtils {

    //当前消息计数
    public static AtomicInteger currentMsgCount = new AtomicInteger(0);
    //前一时间点消息计数
    public static AtomicInteger preMsgCount = new AtomicInteger(0);


    public static Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    public static void addChannel(String uid, Channel channel){
        channelMap.put(uid, channel);
    }

    public static Channel getChannel(String uid) {
        return channelMap.get(uid);
    }

    public static byte[] UnzipData(ByteBuf byteBuf) {
        int n;
        byte[] output = null;
        byte[] buffer = new byte[1024];
        ByteBufInputStream byteBufInputStream = null;
        GZIPInputStream gzipInputStream = null;
        ByteArrayOutputStream out = null;

        try {
            byteBufInputStream = new ByteBufInputStream(byteBuf);
            gzipInputStream = new GZIPInputStream(byteBufInputStream);
            out = new ByteArrayOutputStream();
            while ((n = gzipInputStream.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            out.flush();
            output = out.toByteArray();
        } catch (IOException ignore) {
        } finally {
            try {
                if (byteBufInputStream != null) {
                    byteBufInputStream.close();
                }
            } catch (IOException ignore1) {
            }

            try {
                if (gzipInputStream != null) {
                    gzipInputStream.close();
                }
            } catch (IOException ignore2) {
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore3) {
            }
        }

        return output;
    }

    public static ByteBuf ZipData(byte[] data) {
        ByteBuf output = Unpooled.buffer();
        output.retain();

        ByteBufOutputStream out = null;
        GZIPOutputStream gzip = null;

        try {
            out = new ByteBufOutputStream(output);
            gzip = new GZIPOutputStream(out);
            gzip.write(data);
            gzip.flush();
            out.flush();
        } catch (IOException ignore) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore1) {
            }

            try {
                if (gzip != null) {
                    gzip.close();
                }
            } catch (IOException ignore2) {
            }
        }

        return output;
    }


}
