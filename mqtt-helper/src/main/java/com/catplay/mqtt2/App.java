package com.catplay.mqtt2;

import com.catplay.mqtt2.core.MqttNettyClientFactory;
import com.catplay.mqtt2.conf.MqttProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author wangyong
 * @Date 2020-07-10
 */
public class App {

    public static void main(String[] args) {
        MqttProperties mqttProperties = new MqttProperties();
        mqttProperties.setHost("xxx");
        mqttProperties.setPort(7885);
        mqttProperties.setTopic("dash");
        mqttProperties.setCertificateName("/mqttdebug_test1.cer");

        MqttNettyClientFactory  mqttNettyClientFactory =  new MqttNettyClientFactory(mqttProperties);
        String userName = "11105";
        String uid = "21181440";
        mqttNettyClientFactory.createConnect(uid);
        Map<String, Object> body = new HashMap<>();
        body.put("startTime", System.currentTimeMillis());
        body.put("cocosVersion",10);
        mqttNettyClientFactory.sendMsg(uid, 0, "dashCommonConfig", body);
    }
}
