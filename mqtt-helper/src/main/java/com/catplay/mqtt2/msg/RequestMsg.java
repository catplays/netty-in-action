package com.catplay.mqtt2.msg;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunji on 16/6/25.
 */
public class RequestMsg {
    /** 必须 **/
    private int ver;
    private int appId;
    private String callId;
    private String cmd;
    private long time;

    /** 可选 **/
    private long roomId;
    private String topic;
    private String clientId;
    private String t;
    private String uid;
    private ClientInfo clientInfo;

    private Map<String, Object> body;

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RequestMsg{" +
                "ver=" + ver +
                ", appId=" + appId +
                ", callId='" + callId + '\'' +
                ", cmd='" + cmd + '\'' +
                ", time=" + time +
                ", roomId=" + roomId +
                ", topic='" + topic + '\'' +
                ", clientId='" + clientId + '\'' +
                ", t='" + t + '\'' +
                ", uid='" + uid + '\'' +
                ", clientInfo=" + clientInfo +
                ", body=" + body +
                '}';
    }

//    {
//        "callId": "15677555385178824",
//            "t": "WAztTAQ81UDdQY1RgVTQHZQk4VzJSNgk9CGhTPFAzCzM.",
//            "time": 1567755538517,
//            "uid": "79229065",
//            "ver": 1,
//            "clientId": "a.79229065",
//            "appId": "10002",
//            "cmd": "playerPanel",
//            "topic": "\/meleeCombine\/c2s",
//            "clientInfo": {
//        "deviceId": "962fd8eb-3410-4ae7-925c-0c380209e347",
//                "appVer": "90012",
//                "channelId": "-1",
//                "deviceInfo": "PAFM00(1080*2196)"
//    },
//        "body": {
//        "toUid": "42794619"
//    },
//        "roomId": "0"
//    }
    public static RequestMsg buildMsg(String uid, int random, long roomId, String topic){
        RequestMsg msg = new RequestMsg();
        msg.setVer(1);
        msg.setAppId(10002);
        msg.setCallId(String.valueOf(System.currentTimeMillis()) + random);
        msg.setCmd("wantJoin");
        msg.setTime(System.currentTimeMillis());
        msg.setRoomId(roomId);
        msg.setTopic(topic);
        msg.setCallId("a." + uid);
        msg.setT("WAztTAQ81UDdQY1RgVTQHZQk4VzJSNgk9CGhTPFAzCzM.");
        msg.setUid(uid);

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setAppVer("90012");
        clientInfo.setChannelId(-1);
        clientInfo.setDeviceId("962fd8eb-3410-4ae7-925c-0c380209e347");
        clientInfo.setDeviceInfo("PAFM00(1080*2196)");

        msg.setClientInfo(clientInfo);

        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("roomId", roomId);
        data.put("startTime", System.currentTimeMillis());

        msg.setBody(data);
        return msg;
    }
}
