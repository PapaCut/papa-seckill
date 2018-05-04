package org.papa.canal.server.config;

import com.lmax.disruptor.dsl.Disruptor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.papa.canal.server.CanalListener;
import org.papa.canal.server.CanalMessageConverter;
import org.papa.canal.server.event.CanalHandler;
import org.papa.common.DefaultThreadFactory;
import org.papa.disruptor.DisruptorConfiguration;
import org.papa.seckill.RequestHandler;
import org.papa.seckill.RequestProcessor;
import org.papa.seckill.command.CommandBus;
import org.papa.seckill.event.SimpleCommandHandler;
import org.papa.seckill.event.SimpleGcHandler;
import org.papa.seckill.event.SimpleJmsHandler;
import org.papa.seckill.event.disruptor.*;
import org.papa.seckill.jms.sender.MessageSender;
import org.papa.seckill.jms.sender.activemq.ActiveMQMessageSender;
import org.papa.seckill.jms.sender.disruptor.DisruptorMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.MessageListener;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Created by PaperCut on 2018/3/1.
 */
@Configuration
public class JmsConfiguration {
    @Value("${jms.url}")
    String url;
    @Value("${jms.destination}")
    String cacheTopic;

    @Autowired
    MessageSender messageSender;
    @Autowired
    CommandBus commandBus;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(url);
        return factory;
    }

    @Bean
    public ActiveMQTopic requestQueue() {
        return new ActiveMQTopic(cacheTopic);
    }

    @Bean
    public ActiveMQTopic responseQueue() {
        return new ActiveMQTopic(cacheTopic);
    }


    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
        jmsTemplate.setDefaultDestination(requestQueue());
        return jmsTemplate;
    }

    @Bean
    public MessageSender messageSender() {
        return new DisruptorMessageSender("MessageSender", new ActiveMQMessageSender(jmsTemplate()));
    }

    @Bean
    public DefaultMessageListenerContainer defaultMessageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(activeMQConnectionFactory());
        defaultMessageListenerContainer.setDestination(responseQueue());
        defaultMessageListenerContainer.setMessageListener(productListener());
        return defaultMessageListenerContainer;
    }

    @Bean
    public MessageListener productListener() {
        return new CanalListener(productProcessor(), new CanalMessageConverter());
    }

    /*
    @Bean
    public RequestProcessor productProcessor() {
        return new DisruptorRequestProcessor("CanalRequestProcessor", canalHandler(),
                commandBus, messageSender);
    }
    */

    public RequestProcessor productProcessor() {
        return new AbstractRequestProcessor("CanalRequestProcessor") {
            @Override
            public Supplier<Disruptor> createDisruptor() {
                return () -> {
                    DisruptorRequestHandler business = new DisruptorRequestHandler(canalHandler());
                    DisruptorRequestHandler command = new DisruptorRequestHandler(new SimpleCommandHandler(commandBus));
                    DisruptorRequestHandler jms = new DisruptorRequestHandler(new SimpleJmsHandler(messageSender));
                    DisruptorRequestHandler gc = new DisruptorRequestHandler(new SimpleGcHandler());

                    Disruptor disruptor = new Disruptor(RequestEvent::new,
                            DisruptorConfiguration.DEFAULT_BUFFER_SIZE,
                            new DefaultThreadFactory("canal-request-"));
                    disruptor.handleEventsWith(business).then(command, jms).then(gc);
                    disruptor.setDefaultExceptionHandler(new RequestExceptionHandler());
                    return disruptor;
                };
            }
        };
    }

    @Bean
    public RequestHandler canalHandler() {
        return new CanalHandler();
    }
}
