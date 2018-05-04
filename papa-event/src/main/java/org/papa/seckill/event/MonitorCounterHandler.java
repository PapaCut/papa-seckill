package org.papa.seckill.event;

import org.papa.seckill.RequestHandler;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.papa.seckill.monitor.EventMonitor;
import org.papa.seckill.monitor.MonitorCallback;

/**
 * Created by PaperCut on 2018/4/2.
 * Request Event Metrics
 */
public class MonitorCounterHandler implements RequestHandler {
    private final EventMonitor eventMonitor;

    public MonitorCounterHandler(EventMonitor eventMonitor) {
        this.eventMonitor = eventMonitor;
    }

    @Override
    public RequestEvent onEvent(RequestEvent event) {
        MonitorCallback callback = eventMonitor.monitor(event);
        if(!event.hasErrorOrException()) {
            callback.onSuccess();
        } else {
            callback.onFailure();
        }
        return event;
    }
}
