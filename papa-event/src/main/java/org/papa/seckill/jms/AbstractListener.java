package org.papa.seckill.jms;

import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestProcessor;

/**
 * Created by PaperCut on 2018/3/1.
 */
public abstract class AbstractListener {
    protected final RequestProcessor processor;
    protected final MessageConverter<RequestDto> messageConverter;

    public AbstractListener(RequestProcessor processor, MessageConverter<RequestDto> messageConverter) {
        this.processor = processor;
        this.messageConverter = messageConverter;
    }

    public void onMessage(Object... messages) {
        for(Object message : messages) {
            RequestDto requestDto = messageConverter.readMessage(message);
            processor.getConsumer().accept(requestDto);
        }
    }
}
