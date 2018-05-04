package org.papa.seckill.event;

import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestHandler;
import org.papa.seckill.RequestProcessor;
import org.papa.seckill.command.ChannelEvent;
import org.papa.seckill.command.CommandBus;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.papa.seckill.jms.sender.MessageSender;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/2/26.
 */
@Deprecated
public class SimpleRequestProcessor implements RequestProcessor {
    private final RequestHandler businessHandler;
    private final CommandBus commandBus;
    private final MessageSender messageSender;

    public SimpleRequestProcessor(RequestHandler businessHandler, CommandBus commandBus, MessageSender messageSender) {
        this.businessHandler = businessHandler;
        this.commandBus = commandBus;
        this.messageSender = messageSender;
    }

    @Override
    public Consumer<RequestDto> getConsumer() {
        SimpleCommandHandler commandHandler = new SimpleCommandHandler(commandBus);
        SimpleJmsHandler jmsHandler = new SimpleJmsHandler(messageSender);
        return (request) -> {
            RequestEvent requestEvent = new RequestEvent();
            requestEvent.setRequestDto(request);

            // 执行具体业务
            CompletableFuture<RequestEvent> business = CompletableFuture.supplyAsync(
                    () -> businessHandler.onEvent(requestEvent)
            );

            // 执行command
            CompletableFuture<RequestEvent> command = business.thenComposeAsync(
                    result -> CompletableFuture.supplyAsync(() -> commandHandler.onEvent(result))
            );

            // 执行jms sender
            CompletableFuture<RequestEvent> jms = business.thenComposeAsync(
                    result -> CompletableFuture.supplyAsync(() -> jmsHandler.onEvent(result))
            );
            CompletableFuture result = CompletableFuture.allOf(command, jms);
            result.join();
        };
    }
}
