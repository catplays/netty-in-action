package com.catplay.mqtt2.core;

import com.catplay.mqtt2.msg.RequestMsg;
import com.catplay.mqtt2.msg.WbJSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.apache.log4j.Logger;


/**
 * @Auther: liuwei
 * @Date: 2019/6/4 11:57
 * @Description:
 */

public class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private Logger log = Logger.getLogger(MqttClientHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("MqttClientHandler exceptionCaught channel close", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {

        if (msg.fixedHeader() == null) {
            log.error("MqttServerHandler channelRead empty msg {}"+ msg);
            return;
        }

        try {
            switch (msg.fixedHeader().messageType()) {
                case CONNACK:
//                    MqttConnAckMessage mqttConnAckMessage = (MqttConnAckMessage) msg;
                    break;
                case PUBLISH:
                    MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) msg;
                    byte[] payload = NettyUtils.UnzipData(mqttPublishMessage.payload());
                    String data = new String(payload);
                    RequestMsg requestMsg = WbJSON.fromJson(data, RequestMsg.class);
                    long startTime = Long.parseLong(String.valueOf(requestMsg.getBody().get("startTime")));
                    long time = (System.currentTimeMillis() - startTime);
                    log.info("msg time : " + time + "payload size" + payload.length);
                    System.out.println(("msg time : " + time + "payload size" + payload.length));
                    break;
                case SUBACK:

                    break;
                case PINGRESP:

                    break;
                default:
                    ctx.writeAndFlush("1");
                    break;
            }
        } catch (Exception ex) {
            log.error("MqttServerHandler Bad error in processing the message", ex);
            ctx.fireExceptionCaught(ex);
        }
    }

    private void printPayload(MqttPublishMessage msg) {

        try {
            byte[] payload = NettyUtils.UnzipData(msg.payload());
            String json = new String(payload);
            log.debug("printPayload content: {}"+ json);
        } catch (Exception e) {
            log.debug("printPayload error", e);

        }

    }


}
