package com.catplay.netty.order.common;

import com.catplay.netty.util.JsonUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public abstract class Message<T extends MessageBody> {
    protected MessageHeader messageHeader;
    protected T messageBody;

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public T getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(T messageBody) {
        this.messageBody = messageBody;
    }

    public abstract Class<T> getMessageBodyDecodeClass(int opcode);

    /**
     * 编码
     * @param byteBuf
     */
    public void encode(ByteBuf byteBuf) {
        byteBuf.writeInt(messageHeader.getVersion());
        byteBuf.writeLong(messageHeader.getStreamId());
        byteBuf.writeInt(messageHeader.getOpCode());
        byteBuf.writeBytes(JsonUtil.toJson(messageBody).getBytes());
    }

    /**
     * 解码
     */
    public void decode(ByteBuf msg) {
        MessageHeader messageHeader = MessageHeader.Builder
                .builder()
                .version(msg.readInt())
                .streamId(msg.readLong())
                .opCode(msg.readInt())
                .build();
        this.messageHeader = messageHeader;
        Class<T> bodyClazz = getMessageBodyDecodeClass(messageHeader.getOpCode());
        T body = JsonUtil.fromJson(msg.toString(Charset.forName("UTF-8")), bodyClazz);
        this.messageBody = body;
    }


}
