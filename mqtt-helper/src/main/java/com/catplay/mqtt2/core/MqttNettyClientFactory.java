package com.catplay.mqtt2.core;

import com.catplay.mqtt2.msg.ClientInfo;
import com.catplay.mqtt2.conf.MqttProperties;
import com.catplay.mqtt2.msg.RequestMsg;
import com.catplay.mqtt2.msg.WbJSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Auther: liuwei
 * @Date: 2019/6/4 11:42
 * @Description:
 */
public class MqttNettyClientFactory {

    private NioEventLoopGroup workerGroup = new NioEventLoopGroup(32);
//    private EpollEventLoopGroup workerGroup = new EpollEventLoopGroup(32);



    private Random random = new Random();

    private MqttProperties mqttProperties;

    private Bootstrap bootstrap = new Bootstrap();

    private ChannelFuture channelFuture;
    public MqttNettyClientFactory(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
        init();
    }

    protected void init() {

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast("ssl", createSSLHandler(createSSLContext(mqttProperties.getCertificateName())));
                        pipeline.addLast("MQTTDecoder", new MqttDecoder());
                        pipeline.addLast("MQTTEncoder", MqttEncoder.INSTANCE);
                        pipeline.addLast("MqttClientHandler", new MqttClientHandler());
                    }
                });
    }

    /**
     * 建立连接
     * @param username
     */
    public void createConnect(String username) {
        System.out.println(mqttProperties);
        String[] ips = mqttProperties.getHost().split(",");
        int index = 0;
        if(ips.length > 1){
            index = random.nextInt(ips.length);
        }
        System.out.println(ips[index]);
        ChannelFuture channelFuture = bootstrap.connect(ips[index], mqttProperties.getPort());

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                if (future.isSuccess()) {
                    System.out.println(username + " client connect Success");
                    NettyUtils.addChannel(username, channelFuture.channel());
                    // 建立连接成功之后，发送连接消息
                    sendConnectMessage(channelFuture.channel(), username);
                    // 发送订阅消息
                    sendSubMessage(channelFuture.channel(), username);

                } else {
                    System.out.println(username + " client connect Failed"+future.cause());
                }
            }
        });
        this.channelFuture = channelFuture;
        try {
            channelFuture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static ChannelHandler createSSLHandler(SSLContext sslContext) {
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
        sslEngine.setNeedClientAuth(false);
        return new SslHandler(sslEngine);
    }

    /**
     * 创建SSLContext
     * @param certificate
     * @return
     */
    private static SSLContext createSSLContext(String certificate) {
        try {
            // X.509 是密码学里公钥证书的格式标准。 X.509 证书己应用在包括TLS/SSL在内的众多协议中
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = new FileInputStream(MqttNettyClientFactory.class.getResource(certificate).getFile());

            Certificate ca = cf.generateCertificate(inputStream);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

            ks.load(null, null);//加载 KeyStore
            ks.setCertificateEntry("ca", ca);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            System.out.println("createSSLContext error"+ e);
        }
        return null;
    }

    /**
     * 发送连接消息
     * @param channel
     * @param username
     */
    private void sendConnectMessage(Channel channel, String username) {
        MqttConnectMessage mqttConnectMessage = MqttMessageBuilders.connect()
                .username(username)
                .password("11105".getBytes())
                .clientId("a."+username)
//                .keepAlive(60)
                .build();
        channel.writeAndFlush(mqttConnectMessage);
    }

    /**
     * 订阅topic
     * @param channel
     * @param username
     */
    //约定格式: /PT/uid/集群名/s2c
    private void sendSubMessage(Channel channel, String username) {
        String topic = "/PT/" + username + "/" + mqttProperties.getTopic() + "/s2c";
        MqttSubscribeMessage subscribeMessage = MqttMessageBuilders.subscribe()
                .addSubscription(MqttQoS.AT_MOST_ONCE, topic)
                .messageId(1)
                .build();
        channel.writeAndFlush(subscribeMessage);
    }

    //发送消息 约定格式: /PT/uid/集群名/c2s
    private void sendPublishMsg(String topic, Channel channel, String body) {
        ByteBuf buf = NettyUtils.ZipData(body.getBytes());

        MqttPublishMessage pubPacket = MqttMessageBuilders.publish()
                .messageId(random.nextInt())
                .qos(MqttQoS.AT_MOST_ONCE)
                .payload(buf)
                .retained(false)
                .topicName(topic)
                .build();

        channel.writeAndFlush(pubPacket);
        buf.release();
    }

    /**
     * 发送消息
     * @param uid
     * @param body
     */
    public void sendMsg(String uid,int roomId, String cmd, Map<String, Object> body) {
        RequestMsg msg = new RequestMsg();
        String gatewayTopic = getC2STopic(uid, mqttProperties.getTopic());
        msg.setVer(1);
        msg.setAppId(10002);
        msg.setCallId(String.valueOf(System.currentTimeMillis()) + random);
        msg.setCmd(cmd);
        msg.setTime(System.currentTimeMillis());
        msg.setRoomId(roomId);
        msg.setTopic(mqttProperties.getTopic());
        msg.setCallId("a." + uid);
        msg.setT("WAztTAQ81UDdQY1RgVTQHZQk4VzJSNgk9CGhTPFAzCzM.");
        msg.setUid(uid);

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setAppVer("90012");
        clientInfo.setChannelId(-1);
        clientInfo.setDeviceId("962fd8eb-3410-4ae7-925c-0c380209e347");
        clientInfo.setDeviceInfo("PAFM00(1080*2196)");

        msg.setClientInfo(clientInfo);
        msg.setBody(body);
        sendPublishMsg(gatewayTopic, NettyUtils.getChannel(uid), WbJSON.toJson(msg));
    }


    public static String getS2CTopic(String uid, String cluster) {
        return String.format("/PT/%s/%s/s2c", uid, cluster);
    }

    public static String getC2STopic(String uid, String cluster) {
        return String.format("/PT/%s/%s/c2s", uid, cluster);
    }
}
