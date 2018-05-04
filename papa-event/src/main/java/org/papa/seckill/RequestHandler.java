package org.papa.seckill;

import org.papa.seckill.event.disruptor.RequestEvent;

/**
 * Created by PaperCut on 2018/2/26.
 */
public interface RequestHandler<T> {
    RequestEvent<T> onEvent(RequestEvent<T> event);
}
