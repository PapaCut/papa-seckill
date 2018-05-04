package org.papa.server.product.config;

import org.papa.seckill.command.CommandBus;
import org.papa.seckill.command.DefaultCommandBus;
import org.papa.seckill.command.disruptor.DisruptorCommandChannel;
import org.papa.seckill.spring.AnnotationDriven;
import org.papa.seckill.spring.CommandChannelLifeCycle;
import org.papa.seckill.spring.CommandHandlerBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by PaperCut on 2018/3/1.
 */
@Configuration
public class CommandConfiguration {
    @Bean
    public CommandBus commandBus() {
        return new DefaultCommandBus();
    }

    @Bean
    public CommandHandlerBeanPostProcessor commandHandlerBeanPostProcessor() {
        return new CommandHandlerBeanPostProcessor(commandBus(),
                (cmdExecutor) -> new DisruptorCommandChannel(cmdExecutor));
    }
}
