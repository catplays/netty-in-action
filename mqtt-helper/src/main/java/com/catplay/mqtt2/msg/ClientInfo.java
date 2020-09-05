package com.catplay.mqtt2.msg;

/**
 * Created by sunji on 16/6/25.
 */
public class ClientInfo {
    private String appVer;
    private String deviceId;
    private int channelId;
    private String deviceInfo;

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "appVer='" + appVer + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", channelId=" + channelId +
                ", deviceInfo='" + deviceInfo + '\'' +
                '}';
    }
}
