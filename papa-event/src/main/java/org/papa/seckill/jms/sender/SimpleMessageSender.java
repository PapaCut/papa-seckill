package org.papa.seckill.jms.sender;


import com.alibaba.fastjson.JSON;
import org.papa.seckill.command.ChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class SimpleMessageSender implements MessageSender{
    private static final Logger logger = LoggerFactory.getLogger(SimpleMessageSender.class);

    private final MessageSender messageSender;
    public SimpleMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void sendMessage(Object payload) {
        messageSender.sendMessage(payload);
        logger.info("Send message {}.", JSON.toJSONString(payload));
    }
}
