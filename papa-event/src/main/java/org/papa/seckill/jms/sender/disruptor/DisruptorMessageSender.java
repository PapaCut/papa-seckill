package org.papa.seckill.jms.sender.disruptor;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.papa.common.DefaultThreadFactory;
import org.papa.disruptor.AbstractDisruptorLifecycle;
import org.papa.disruptor.DisruptorConfiguration;
import org.papa.seckill.command.ChannelEvent;
import org.papa.seckill.jms.sender.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class DisruptorMessageSender extends AbstractDisruptorLifecycle implements MessageSender, EventHandler<PayloadEvent>{
    private static final Logger logger = LoggerFactory.getLogger(DisruptorMessageSender.class);

    private PayloadEventProducer producer;

    private final MessageSender decorate;

    private ExceptionHandler<PayloadEvent> exceptionHandler;

    public ExceptionHandler<PayloadEvent> getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler<PayloadEvent> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public DisruptorMessageSender(String disruptorName, MessageSender decorate) {
        this(disruptorName, decorate, DisruptorConfiguration.builder().build());
    }

    public DisruptorMessageSender(String disruptorName, MessageSender decorate, DisruptorConfiguration configuration) {
        super(disruptorName, configuration);
        this.decorate = decorate;
    }

    @Override
    public void sendMessage(Object payload) {
        producer.onData(payload);
    }

    @Override
    public void onEvent(PayloadEvent payloadEvent, long l, boolean b) throws Exception {
        Object payload = payloadEvent.getPayload();
        if(payload == null) {
            throw new RuntimeException("The payload can't be null");
        }
        logger.info("Send message {}.", JSON.toJSONString(payload));

        // do send message.
        decorate.sendMessage(payload);

        payloadEvent.setPayload(null);
    }

    @Override
    public Supplier<Disruptor> createDisruptor() {
        return () -> {
            Disruptor disruptor = new Disruptor(
                    PayloadEvent::new,
                    DisruptorConfiguration.DEFAULT_BUFFER_SIZE,
                    new DefaultThreadFactory("msg-sender"));
            disruptor.handleEventsWith(this);
            // 设置异常处理
            if(this.getExceptionHandler() == null) {
                disruptor.setDefaultExceptionHandler(new SenderExceptionHandler());
            } else {
                disruptor.setDefaultExceptionHandler(this.getExceptionHandler());
            }
            return disruptor;
        };
    }

    @Override
    protected void doStart() {
        this.producer = new PayloadEventProducer(getDisruptor().getRingBuffer());
    }

    @Override
    protected void doStop() {}

    /**
     * 异常处理
     */
    class SenderExceptionHandler implements ExceptionHandler<PayloadEvent>{
        private final Logger logger = LoggerFactory.getLogger(SenderExceptionHandler.class);

        @Override
        public void handleEventException(Throwable throwable, long l, PayloadEvent payloadEvent) {
            logger.error("Failed to send message handler. event:{}, msg:{}",
                    JSON.toJSONString(payloadEvent), ExceptionUtils.getMessage(throwable));
        }

        @Override
        public void handleOnStartException(Throwable throwable) {
            logger.error("Failed to start the send message handler.", throwable);
        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {
            logger.error("Failed to shutdown the send message handler.", throwable);
        }
    }
}
