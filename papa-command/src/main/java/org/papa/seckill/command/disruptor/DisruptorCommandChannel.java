package org.papa.seckill.command.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import org.papa.disruptor.AbstractDisruptorLifecycle;
import org.papa.disruptor.DisruptorConfiguration;
import org.papa.seckill.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by PaperCut on 2018/2/19.
 */
public class DisruptorCommandChannel extends AbstractDisruptorLifecycle implements CommandChannel {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorCommandChannel.class);

    private CommandEventProducer commandEventProducer;
    private CommandExecutor commandExecutor;
    private CommandBuffer commandBuffer;

    public DisruptorCommandChannel(CommandExecutor commandExecutor) {
        this("DisruptorCommandChannel" + "-" + commandExecutor.getClass().getName(), commandExecutor);
    }

    public DisruptorCommandChannel(String disruptorName, CommandExecutor commandExecutor) {
        this(disruptorName, commandExecutor, new DefaultCommandBuffer(), DisruptorConfiguration.builder().build());
    }

    public DisruptorCommandChannel(String disruptorName, CommandExecutor commandExecutor, CommandBuffer commandBuffer, DisruptorConfiguration disruptorConfiguration) {
        super(disruptorName, disruptorConfiguration);
        this.commandExecutor = commandExecutor;
        this.commandBuffer = commandBuffer;
    }

    @Override
    public Consumer<Command> getConsumer() {
        return (cmd) -> commandEventProducer.onData(cmd);
    }

    @Override
    protected void doStart() {
        this.commandEventProducer = new CommandEventProducer(getDisruptor().getRingBuffer());
    }

    @Override
    protected void doStop() { }

    @Override
    public Supplier<Disruptor> createDisruptor() {
        return () -> {
            Disruptor disruptor = new Disruptor(CommandEvent::new,
                    getConfiguration().getBufferSize(),
                    getExecutorService(),
                    getConfiguration().getProducerType(),
                    getConfiguration().getWaitStrategy());
            disruptor.handleEventsWith(new CommandEventHandler(commandBuffer, commandExecutor))
                    .then(new CommandGcHandler());
            disruptor.setDefaultExceptionHandler(new CommandExceptionHandler());
            return disruptor;
        };
    }
}
