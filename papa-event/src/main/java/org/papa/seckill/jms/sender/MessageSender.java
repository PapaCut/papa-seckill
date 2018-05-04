package org.papa.seckill.jms.sender;

/**
 * Created by PaperCut on 2018/2/26.
 * 消息队列发送接口
 */
public interface MessageSender {
    void sendMessage(Object payload);
}
