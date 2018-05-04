package org.papa.client.product.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.MessageListener;

/**
 * Created by PaperCut on 2018/3/1.
 */
@Configuration
public class JmsConfiguration {
    @Value("${jms.url}")
    String url;
    @Value("${jms.requestQueue}")
    String requestQueue;
    @Value("${jms.responseQueue}")
    String responseQueue;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(url);
        return factory;
    }

    @Bean
    public ActiveMQQueue requestQueue() {
        return new ActiveMQQueue(requestQueue);
    }

    @Bean
    public ActiveMQQueue responseQueue() {
        return new ActiveMQQueue(responseQueue);
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
        jmsTemplate.setDefaultDestination(requestQueue());
        return jmsTemplate;
    }

    @Bean
    public DefaultMessageListenerContainer defaultMessageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(activeMQConnectionFactory());
        defaultMessageListenerContainer.setDestination(responseQueue());
        defaultMessageListenerContainer.setMessageListener(responseListener());
        return defaultMessageListenerContainer;
    }

    @Bean
    public MessageListener responseListener() {
        return (msg) -> {
            System.out.println("response success.");
        };
    }
}
