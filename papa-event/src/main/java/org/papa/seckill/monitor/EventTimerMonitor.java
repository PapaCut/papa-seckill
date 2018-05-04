package org.papa.seckill.monitor;

import com.codahale.metrics.*;
import org.papa.seckill.event.disruptor.RequestEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PaperCut on 2018/4/12.
 */
public class EventTimerMonitor implements EventMonitor, MetricSet{
    private final Timer allTimer;
    private final Timer successTimer;
    private final Timer failureTimer;

    public EventTimerMonitor(Clock clock) {
        allTimer = new Timer(new ExponentiallyDecayingReservoir(), clock);
        successTimer = new Timer(new ExponentiallyDecayingReservoir(), clock);
        failureTimer = new Timer(new ExponentiallyDecayingReservoir(), clock);
    }

    @Override
    public MonitorCallback monitor(RequestEvent event) {
        final Timer.Context allTimerContext = allTimer.time();
        final Timer.Context successTimerContext = successTimer.time();
        final Timer.Context failureTimerContext = failureTimer.time();

        return new MonitorCallback() {
            @Override
            public void onSuccess() {
                allTimerContext.stop();
                successTimerContext.stop();
            }

            @Override
            public void onFailure() {
                allTimerContext.stop();
                failureTimerContext.stop();
            }
        };
    }

    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metrics = new HashMap<>();
        metrics.put("allTimer", allTimer);
        metrics.put("successTimer", successTimer);
        metrics.put("failutreTimer", failureTimer);
        return metrics;
    }
}
