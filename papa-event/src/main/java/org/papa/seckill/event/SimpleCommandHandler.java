package org.papa.seckill.event;

import org.papa.seckill.RequestHandler;
import org.papa.seckill.command.Command;
import org.papa.seckill.command.CommandBus;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class SimpleCommandHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(SimpleCommandHandler.class);

    private final CommandBus commandBus;

    public SimpleCommandHandler(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @Override
    public RequestEvent onEvent(RequestEvent event) {
        if(event.hasErrorOrException()) {
            return event;
        }

        List<Command> commandList = event.getCommandCollector().getCommands();
        if(!CollectionUtils.isEmpty(commandList)) {
            for (Command command : commandList) {
                commandBus.dispatch(command);
            }
        }
        return event;
    }
}
