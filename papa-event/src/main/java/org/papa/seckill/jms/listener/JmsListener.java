package org.papa.seckill.jms.listener;

import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestProcessor;
import org.papa.seckill.jms.AbstractListener;
import org.papa.seckill.jms.MessageConverter;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by PaperCut on 2018/3/1.
 */
public class JmsListener extends AbstractListener implements MessageListener{
    public JmsListener(RequestProcessor processor, MessageConverter<RequestDto> messageConverter) {
        super(processor, messageConverter);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
    }
}
