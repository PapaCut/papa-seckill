package org.papa.seckill.jms.sender.activemq;

import org.papa.seckill.jms.sender.MessageSender;
import org.springframework.jms.core.JmsTemplate;

/**
 * Created by PaperCut on 2018/3/1.
 */
public class ActiveMQMessageSender implements MessageSender {
    private final JmsTemplate jmsTemplate;

    public ActiveMQMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendMessage(Object payload) {
        jmsTemplate.convertAndSend(payload);
    }
}
