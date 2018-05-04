package org.papa.seckill.spring;

import org.papa.seckill.command.CommandExecutor;
import org.papa.seckill.command.CommandBus;
import org.papa.seckill.command.CommandChannel;
import org.papa.seckill.command.annotation.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.function.Function;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class CommandHandlerBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandlerBeanPostProcessor.class);

    private final Function<CommandExecutor, CommandChannel> channelFactory;
    private final CommandBus commandBus;
    private ApplicationContext applicationContext;

    public CommandHandlerBeanPostProcessor(CommandBus commandBus, Function<CommandExecutor, CommandChannel> channelFactory) {
        this.commandBus = commandBus;
        this.channelFactory = channelFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if(o instanceof CommandExecutor) {
            CommandHandler handler = AnnotationUtils.findAnnotation(o.getClass(), CommandHandler.class);
            if (handler != null) {
                CommandChannel channel = channelFactory.apply((CommandExecutor)o);
                commandBus.subscribe(handler.command(), channel);
                String beanName = handler.command().getSimpleName() + "-" + channel.getClass().getSimpleName();
                AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
                ((SingletonBeanRegistry)beanFactory).registerSingleton(beanName, channel);
                beanFactory.initializeBean(channel, beanName);
                logger.info("Succeed register command channel. The command: {}, channel: {}", handler.command().getName(), channel.getClass().getName());
            }
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
