package org.papa.canal.server;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.papa.canal.CacheMessage;
import org.papa.seckill.RequestDto;
import org.papa.seckill.jms.MessageConverter;

import javax.jms.JMSException;

/**
 * Created by PaperCut on 2018/3/4.
 * 队列消息转换器
 */
public class CanalMessageConverter implements MessageConverter<RequestDto> {
    @Override
    public RequestDto readMessage(Object message) {
        ActiveMQObjectMessage msg = (ActiveMQObjectMessage)message;
        try {
            if(msg.getObject() != null && msg.getObject() instanceof RequestDto) {
                RequestDto<CacheMessage> requestDto = (RequestDto) msg.getObject();
                return requestDto;
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object toMessage(RequestDto obj) {
        return obj;
    }
}
