package org.papa.seckill.monitor;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PaperCut on 2018/4/12.
 */
public class EventCounterMonitor implements MetricSet, EventMonitor {
    private static final Logger logger = LoggerFactory.getLogger(EventCounterMonitor.class);

    private final Counter ingestedCounter = new Counter();
    private final Counter successCounter = new Counter();
    private final Counter failureCounter = new Counter();


    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metricSet = new HashMap<>();
        metricSet.put("ingestedCounter", ingestedCounter);
        metricSet.put("successCounter", successCounter);
        metricSet.put("failureCounter", failureCounter);
        return metricSet;
    }

    @Override
    public MonitorCallback monitor(RequestEvent event) {
        ingestedCounter.inc();
        return new MonitorCallback(){
            @Override
            public void onSuccess() {
                successCounter.inc();
            }

            @Override
            public void onFailure() {
                failureCounter.inc();
            }
        };
    }
}
