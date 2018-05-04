package org.papa.seckill.event.disruptor;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;
import org.papa.seckill.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class DisruptorRequestHandler implements RequestHandler,EventHandler<RequestEvent>{
    private static final Logger logger = LoggerFactory.getLogger(DisruptorRequestHandler.class);

    private final RequestHandler decorator;

    public DisruptorRequestHandler(RequestHandler decorator) {
        this.decorator = decorator;
    }

    @Override
    public RequestEvent onEvent(RequestEvent event) {
        if(logger.isDebugEnabled()) {
            logger.debug("{} - requestEvent: {}", decorator.getClass().getName(), JSON.toJSONString(event));
        }

        decorator.onEvent(event);
        return event;
    }

    @Override
    public void onEvent(RequestEvent requestEvent, long l, boolean b) throws Exception {
        onEvent(requestEvent);
    }
}
