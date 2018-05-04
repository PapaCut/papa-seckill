package org.papa.disruptor.config;

import org.papa.disruptor.DisruptorContainer;
import org.papa.disruptor.spring.DisruptorSupportBeanPostProcessor;
import org.papa.disruptor.spring.DisruptorSupportLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by PaperCut on 2018/3/26.
 */
@Configuration
public class DisruptorAutoConfiguration {
    @Bean
    public DisruptorSupportBeanPostProcessor disruptorSupportBeanPostProcessor(DisruptorContainer disruptorContainer) {
        return new DisruptorSupportBeanPostProcessor(disruptorContainer);
    }

    @Bean
    public DisruptorContainer disruptorContainer() {
        return new DisruptorContainer();
    }

    @Bean
    public DisruptorSupportLifecycle disruptorSupportLifecycle(DisruptorContainer disruptorContainer) {
        return new DisruptorSupportLifecycle(disruptorContainer, 0);
    }
}
