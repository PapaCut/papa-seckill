package org.papa.seckill.spring;

import org.papa.seckill.command.ChannelEvent;
import org.papa.seckill.command.CommandBus;
import org.springframework.context.SmartLifecycle;

/**
 * Created by PaperCut on 2018/2/26.
 */
@Deprecated
public class CommandChannelLifeCycle implements SmartLifecycle{
    private volatile boolean running = false;
    private final CommandBus commandBus;
    private final int phase;

    public CommandChannelLifeCycle(CommandBus commandBus, int phase) {
        this.commandBus = commandBus;
        this.phase = phase;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        this.stop();
        runnable.run();
    }

    @Override
    public void start() {
        this.running = true;
//        this.commandBus.publishEvent(ChannelEvent.START);
    }

    @Override
    public void stop() {
        this.running = false;
//        this.commandBus.publishEvent(ChannelEvent.SHUTDOWN);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return phase;
    }
}
