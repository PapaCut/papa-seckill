package org.papa.canal.server;

import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestProcessor;
import org.papa.seckill.jms.AbstractListener;
import org.papa.seckill.jms.MessageConverter;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by PaperCut on 2018/3/4.
 * Canal的消息监听器
 */
public class CanalListener extends AbstractListener implements MessageListener {
    public CanalListener(RequestProcessor processor, MessageConverter<RequestDto> messageConverter) {
        super(processor, messageConverter);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
    }
}
