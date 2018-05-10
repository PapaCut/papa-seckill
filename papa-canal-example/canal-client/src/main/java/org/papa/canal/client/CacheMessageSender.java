package org.papa.canal.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import org.papa.canal.CacheMessage;
import org.papa.canal.CanalEvent;
import org.papa.canal.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

/**
 * Created by PaperCut on 2018/2/6.
 */
public class CacheMessageSender implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(CacheMessageSender.class);

    private JmsTemplate jmsTemplate;
    private ApplicationContext applicationContext;

    public void send(CanalEvent event) {
        CacheMessage message = new CacheMessage();
        message.setMsgId(UUID.randomUUID().toString());

        if(event.getEventType() == CanalEntry.EventType.DELETE) {
            message.setEventType(EventType.DELETE);
        } else if (event.getEventType() == CanalEntry.EventType.INSERT) {
            message.setEventType(EventType.INSERT);
        } else {
            message.setEventType(EventType.UPDATE);
        }

        message.setCacheKey(event.getTableName());
        message.setCacheField(event.getId().toString());

        JSONObject jsonObj = new JSONObject();
        for(CanalEntry.Column column : event.getAfterColumnsList()) {
            jsonObj.put(column.getName(), column.getValue());
        }
        message.setCacheValue(jsonObj.toJSONString());

        if(logger.isDebugEnabled()) {
            logger.debug("The msg: {}", JSON.toJSONString(message));
        }

        jmsTemplate.convertAndSend(message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        if(jmsTemplate == null) {
            throw new IllegalStateException("Not found jmsTemplate from beanFactory");
        }
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
