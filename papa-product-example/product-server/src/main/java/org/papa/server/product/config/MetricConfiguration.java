package org.papa.server.product.config;

import com.codahale.metrics.MetricRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by PaperCut on 2018/4/13.
 */
@Configuration
public class MetricConfiguration {
    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }
}
