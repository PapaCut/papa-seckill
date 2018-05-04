package org.papa.seckill.monitor;

import org.papa.seckill.event.disruptor.RequestEvent;

/**
 * Created by PaperCut on 2018/4/12.
 */
public interface EventMonitor {
    MonitorCallback monitor(RequestEvent event);
}
