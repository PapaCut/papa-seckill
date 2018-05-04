package org.papa.seckill.command.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.papa.seckill.command.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PaperCut on 2018/2/14.
 */
public class CommandExceptionHandler implements ExceptionHandler<CommandEvent> {
    private static final Logger logger = LoggerFactory.getLogger(CommandExceptionHandler.class);

    @Override
    public void handleEventException(Throwable throwable, long l, CommandEvent commandEvent) {
        logger.error("{}: {}, {}", commandEvent.getCommand().getClass().getName(),
                commandEvent.getCommand().getId(), ExceptionUtils.getStackTrace(throwable));
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        logger.error("Failed to start command handler.", throwable);
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        logger.error("Failed to shutdown command handler.", throwable);
    }
}
