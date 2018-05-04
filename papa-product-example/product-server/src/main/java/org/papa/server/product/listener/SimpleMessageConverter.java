package org.papa.server.product.listener;

import com.alibaba.fastjson.JSON;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.papa.product.ProductDto;
import org.papa.seckill.RequestDto;
import org.papa.seckill.jms.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.util.Optional;

/**
 * Created by PaperCut on 2018/3/1.
 */
public class SimpleMessageConverter implements MessageConverter<RequestDto> {
    private static final Logger logger = LoggerFactory.getLogger(SimpleMessageConverter.class);

    @Override
    public RequestDto<ProductDto> readMessage(Object message) {
        if(message == null) {
            throw new IllegalArgumentException("Can not convert this message.");
        }
        try {
            ActiveMQObjectMessage msg = (ActiveMQObjectMessage) message;
            if(msg.getObject() != null && msg.getObject() instanceof RequestDto) {
                RequestDto<ProductDto> requestDto = (RequestDto) msg.getObject();
                return requestDto;
            }
        } catch (JMSException e) {
            logger.error("Failed to read message.{}", JSON.toJSONString(message));
        }
        return null;
    }

    @Override
    public Object toMessage(RequestDto obj) {
        return obj;
    }
}
