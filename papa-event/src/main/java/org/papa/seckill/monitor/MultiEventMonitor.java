package org.papa.seckill.monitor;

import org.papa.seckill.event.disruptor.RequestEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by PaperCut on 2018/4/12.
 */
public class MultiEventMonitor implements EventMonitor {
    private final List<EventMonitor> monitors;

    public MultiEventMonitor(EventMonitor... monitor) {
        monitors = new ArrayList<>(monitor.length);
        monitors.addAll(Arrays.asList(monitor));
    }

    @Override
    public MonitorCallback monitor(RequestEvent event) {
        List<MonitorCallback> monitorCallbacks = monitors.stream().map((monitor) -> monitor.monitor(event)).collect(Collectors.toList());
        return new MonitorCallback(){
            @Override
            public void onSuccess() {
                monitorCallbacks.forEach(MonitorCallback::onSuccess);
            }

            @Override
            public void onFailure() {
                monitorCallbacks.forEach(MonitorCallback::onFailure);
            }
        };
    }
}
