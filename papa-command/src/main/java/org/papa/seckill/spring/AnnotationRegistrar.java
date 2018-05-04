package org.papa.seckill.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by PaperCut on 2018/4/8.
 */
public class AnnotationRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        registerCommandHandler(beanDefinitionRegistry);
    }

    public void registerCommandHandler(BeanDefinitionRegistry registry) {
        GenericBeanDefinition commandBeanDefinition = new GenericBeanDefinition();
        commandBeanDefinition.setBeanClass(CommandHandlerBeanPostProcessor.class);
        registry.registerBeanDefinition("command_handler_bean_post_processor", commandBeanDefinition);
    }
}
