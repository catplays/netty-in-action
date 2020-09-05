package com.catplay.netty.order.common;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class MessageHeader {
    private int version = 1;
    private int opCode;//操作码
    private long streamId;//流水号

    public static class Builder{
        private MessageHeader messageHeader;

        public Builder(MessageHeader messageHeader) {
            this.messageHeader = messageHeader;
        }

        public static Builder builder() {
            return new Builder(new MessageHeader());
        }

        public Builder version(int version) {
            messageHeader.setVersion(version);
            return this;
        }

        public Builder opCode(int opCode) {
            messageHeader.setOpCode(opCode);
            return this;
        }

        public Builder streamId(long streamId) {
            messageHeader.setStreamId(streamId);
            return this;
        }

        public MessageHeader build() {
            return messageHeader;
        }

    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public long getStreamId() {
        return streamId;
    }

    public void setStreamId(long streamId) {
        this.streamId = streamId;
    }
}
