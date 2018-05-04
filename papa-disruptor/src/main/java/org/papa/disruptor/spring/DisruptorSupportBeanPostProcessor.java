package org.papa.disruptor.spring;

import org.papa.disruptor.DisruptorContainer;
import org.papa.disruptor.DisruptorLifecycle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by PaperCut on 2018/3/26.
 */
public class DisruptorSupportBeanPostProcessor implements BeanPostProcessor {
    private final DisruptorContainer disruptorContainer;
    public DisruptorSupportBeanPostProcessor(DisruptorContainer disruptorContainer) {
        this.disruptorContainer = disruptorContainer;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if(o instanceof DisruptorLifecycle) {
            DisruptorLifecycle disruptorLifecycle = (DisruptorLifecycle)o;
            disruptorContainer.register(disruptorLifecycle.getDisruptorName(), disruptorLifecycle);
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
