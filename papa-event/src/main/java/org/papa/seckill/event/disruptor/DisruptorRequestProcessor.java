package org.papa.seckill.event.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import org.papa.common.DefaultThreadFactory;
import org.papa.disruptor.AbstractDisruptorLifecycle;
import org.papa.disruptor.DisruptorConfiguration;
import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestHandler;
import org.papa.seckill.RequestProcessor;
import org.papa.seckill.command.ChannelEvent;
import org.papa.seckill.command.CommandBus;
import org.papa.seckill.event.SimpleCommandHandler;
import org.papa.seckill.event.SimpleGcHandler;
import org.papa.seckill.event.SimpleJmsHandler;
import org.papa.seckill.jms.sender.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by PaperCut on 2018/2/26.
 */
// 发现这种写的还是太牵强了.2018-03-26 16:08:38
@Deprecated
public class DisruptorRequestProcessor extends AbstractDisruptorLifecycle implements RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorRequestProcessor.class);

    private RequestProducer producer;
    private final RequestHandler businessHandler;
    private final CommandBus commandBus;
    private final MessageSender messageSender;

    public DisruptorRequestProcessor(String disruptorName, RequestHandler businessHandler, CommandBus commandBus, MessageSender messageSender) {
        this(disruptorName, businessHandler, commandBus, messageSender, DisruptorConfiguration.builder().build());
    }

    public DisruptorRequestProcessor(String disruptorName, RequestHandler businessHandler, CommandBus commandBus,
                                     MessageSender messageSender, DisruptorConfiguration configuration) {
        super(disruptorName, configuration);
        this.businessHandler = businessHandler;
        this.commandBus = commandBus;
        this.messageSender = messageSender;
    }

    public RequestHandler getBusinessHandler() {
        return businessHandler;
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

    @Override
    public Supplier<Disruptor> createDisruptor() {
        return () -> {
            DisruptorRequestHandler business = new DisruptorRequestHandler(getBusinessHandler());
            DisruptorRequestHandler command = new DisruptorRequestHandler(new SimpleCommandHandler(commandBus));
            DisruptorRequestHandler jms = new DisruptorRequestHandler(new SimpleJmsHandler(messageSender));
            DisruptorRequestHandler gc = new DisruptorRequestHandler(new SimpleGcHandler());
            Disruptor disruptor = new Disruptor(RequestEvent::new,
                    DisruptorConfiguration.DEFAULT_BUFFER_SIZE,
                    new DefaultThreadFactory("request-"));
            disruptor.handleEventsWith(business).then(command, jms).then(gc);
            disruptor.setDefaultExceptionHandler(new RequestExceptionHandler());
            return disruptor;
        };
    }
}
