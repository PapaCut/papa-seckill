package org.papa.server.product.listener;

import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestProcessor;
import org.papa.seckill.jms.AbstractListener;
import org.papa.seckill.jms.MessageConverter;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by PaperCut on 2018/3/1.
 */
public class ProductMessageListener extends AbstractListener implements MessageListener{
    public ProductMessageListener(RequestProcessor processor, MessageConverter<RequestDto> messageConverter) {
        super(processor, messageConverter);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
    }
}
