package org.papa.server.product.config;

import com.codahale.metrics.Clock;
import com.codahale.metrics.MetricRegistry;
import com.lmax.disruptor.dsl.Disruptor;
import com.sun.jndi.ldap.pool.PooledConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.papa.common.DefaultThreadFactory;
import org.papa.disruptor.DisruptorConfiguration;
import org.papa.seckill.RequestHandler;
import org.papa.seckill.RequestProcessor;
import org.papa.seckill.command.CommandBus;
import org.papa.seckill.event.SimpleCommandHandler;
import org.papa.seckill.event.SimpleGcHandler;
import org.papa.seckill.event.SimpleJmsHandler;
import org.papa.seckill.event.MonitorCounterHandler;
import org.papa.seckill.event.disruptor.AbstractRequestProcessor;
import org.papa.seckill.event.disruptor.DisruptorRequestHandler;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.papa.seckill.event.disruptor.RequestExceptionHandler;
import org.papa.seckill.jms.sender.MessageSender;
import org.papa.seckill.jms.sender.activemq.ActiveMQMessageSender;
import org.papa.seckill.jms.sender.disruptor.DisruptorMessageSender;
import org.papa.seckill.monitor.EventCounterMonitor;
import org.papa.seckill.monitor.EventMonitor;
import org.papa.seckill.monitor.EventTimerMonitor;
import org.papa.seckill.monitor.MultiEventMonitor;
import org.papa.server.product.event.ProductHandler;
import org.papa.server.product.listener.ProductMessageListener;
import org.papa.server.product.listener.SimpleMessageConverter;
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
    @Value("${jms.requestQueue}")
    String requestQueue;
    @Value("${jms.responseQueue}")
    String responseQueue;

    @Autowired
    MessageSender messageSender;
    @Autowired
    CommandBus commandBus;

    @Autowired
    MetricRegistry metricRegistry;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(url);
        factory.setMaxThreadPoolSize(100);
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
        jmsTemplate.setDefaultDestination(responseQueue());
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
        defaultMessageListenerContainer.setDestination(requestQueue());
        defaultMessageListenerContainer.setMessageListener(productListener());
        defaultMessageListenerContainer.setConcurrentConsumers(5);
        defaultMessageListenerContainer.setSessionAcknowledgeMode(4);
        return defaultMessageListenerContainer;
    }

    @Bean
    public MessageListener productListener() {
        return new ProductMessageListener(productProcessor(), new SimpleMessageConverter());
    }

    @Bean
    public RequestProcessor productProcessor() {
        return new AbstractRequestProcessor("ProductRequestProcessor") {
            @Override
            public Supplier<Disruptor> createDisruptor() {
                return () -> {
                    DisruptorRequestHandler business = new DisruptorRequestHandler(productHandler());
                    DisruptorRequestHandler command = new DisruptorRequestHandler(new SimpleCommandHandler(commandBus));
                    DisruptorRequestHandler jms = new DisruptorRequestHandler(new SimpleJmsHandler(messageSender));
                    DisruptorRequestHandler gc = new DisruptorRequestHandler(new SimpleGcHandler());
                    DisruptorRequestHandler monitor = new DisruptorRequestHandler(new MonitorCounterHandler(eventMonitor()));

                    Disruptor disruptor = new Disruptor(
                            RequestEvent::new,
                            DisruptorConfiguration.DEFAULT_BUFFER_SIZE,
                            new DefaultThreadFactory("product-request-"));
                    disruptor.handleEventsWith(business).then(command, jms).then(gc).then(monitor);
                    disruptor.setDefaultExceptionHandler(new RequestExceptionHandler());
                    return disruptor;
                };
            }
        };
    }

    public EventMonitor eventMonitor() {
        EventCounterMonitor counter = new EventCounterMonitor();
        EventTimerMonitor timer = new EventTimerMonitor(Clock.defaultClock());
        metricRegistry.register("event_counter", counter);
        metricRegistry.register("event_timer", timer);
        MultiEventMonitor monitor = new MultiEventMonitor(counter, timer);
        return monitor;
    }

    @Bean
    public RequestHandler productHandler() {
        return new ProductHandler();
    }
}
