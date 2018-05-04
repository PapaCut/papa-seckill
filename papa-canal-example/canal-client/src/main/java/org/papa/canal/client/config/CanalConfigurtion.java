package org.papa.canal.client.config;//package org.papa.canal;

import org.papa.canal.client.CacheMessageSender;
import org.papa.canal.client.spring.DefaultCanalContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by PaperCut on 2018/2/6.
 */
@Configuration
public class CanalConfigurtion {
    @Autowired
    CanalProperties config;

    @Bean
    public DefaultCanalContainer defaultCanalContainer() {
        return new DefaultCanalContainer("CanalContainer", 1, config);
    }

    @Bean
    public CacheMessageSender cacheMessageSender(DefaultCanalContainer defaultCanalContainer) {
        CacheMessageSender sender = new CacheMessageSender();
        defaultCanalContainer.subscribe(sender::send);
        return sender;
    }


}
