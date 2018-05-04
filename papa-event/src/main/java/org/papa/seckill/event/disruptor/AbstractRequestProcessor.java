package org.papa.seckill.event.disruptor;

import org.papa.disruptor.AbstractDisruptorLifecycle;
import org.papa.disruptor.DisruptorConfiguration;
import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/3/26.
 * 抽象请求处理器
 */
public abstract class AbstractRequestProcessor extends AbstractDisruptorLifecycle implements RequestProcessor{
    private static final Logger logger = LoggerFactory.getLogger(AbstractRequestProcessor.class);

    protected RequestProducer producer;

    public AbstractRequestProcessor(String disruptorName) {
        this(disruptorName, DisruptorConfiguration.builder().build());
    }

    public AbstractRequestProcessor(String disruptorName, DisruptorConfiguration configuration) {
        super(disruptorName, configuration);
    }

    @Override
    public Consumer<RequestDto> getConsumer() {
        return (request) -> {
            producer.onData(request);
        };
    }

    @Override
    protected void doStart() {
        this.producer = new RequestProducer(getDisruptor().getRingBuffer());
    }

    @Override
    protected void doStop() { }
}
