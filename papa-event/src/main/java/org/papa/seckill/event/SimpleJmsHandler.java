package org.papa.seckill.event;

import org.papa.seckill.RequestHandler;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.papa.seckill.jms.sender.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class SimpleJmsHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJmsHandler.class);

    private final MessageSender messageSender;

    public SimpleJmsHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public RequestEvent onEvent(RequestEvent event) {
        if(event.hasErrorOrException()) {
            return event;
        }
        messageSender.sendMessage(event.getResponseDto());
        return event;
    }
}
