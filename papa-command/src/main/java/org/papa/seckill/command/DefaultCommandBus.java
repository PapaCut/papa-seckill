package org.papa.seckill.command;

import org.papa.metrics.command.CommandMonitor;
import org.papa.metrics.command.NoCommandMonitor;
import org.papa.seckill.command.exception.ChannelNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by PaperCut on 2018/2/27.
 */
public class DefaultCommandBus implements CommandBus{
    private Map<Class, CommandChannel> channels = new ConcurrentHashMap<>();

    private CommandMonitor monitor = NoCommandMonitor.INSTANCE;

    @Override
    public void subscribe(Class<? extends Command> cmdClass, CommandChannel channel) {
        channels.putIfAbsent(cmdClass, channel);
    }

    @Override
    public void dispatch(Command command) {
        Assert.notNull(command, "The command can't be null");

        CommandChannel channel = channels.get(command.getClass());
        if(channel == null) {
            throw new ChannelNotFoundException(
                    String.format("The command channel [%s] not found", command.getClass().getName()));
        }

        try {
            channel.getConsumer().accept(command);
        } catch (Exception e) {

        }
    }
}
