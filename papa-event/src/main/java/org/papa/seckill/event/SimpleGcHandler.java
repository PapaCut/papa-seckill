package org.papa.seckill.event;

import org.papa.seckill.RequestHandler;
import org.papa.seckill.event.disruptor.RequestEvent;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class SimpleGcHandler implements RequestHandler {
    @Override
    public RequestEvent onEvent(RequestEvent event) {
        event.clearForGc();
        return event;
    }
}
