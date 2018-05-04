package org.papa.metrics.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.papa.metrics.spring.ConsoleMetricsLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by PaperCut on 2018/4/4.
 */
@Configuration
public class MetricsConfiguration {
    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    /*
    @Bean
    public ScheduledReporter reporter(MetricRegistry metricRegistry) {
        return ConsoleReporter.forRegistry(metricRegistry).build();
    }
    */

    @Bean
    public GraphiteReporter reporter() {
        return null;
    }

    @Bean
    public ConsoleMetricsLifecycle consoleMetricsLifecycle(GraphiteReporter reporter) {
        return new ConsoleMetricsLifecycle(reporter, 0);
    }
}
