package org.papa.disruptor.spring;

import org.papa.disruptor.DisruptorContainer;
import org.papa.disruptor.DisruptorEvent;
import org.springframework.context.SmartLifecycle;

/**
 * Created by PaperCut on 2018/3/26.
 */
public class DisruptorSupportLifecycle implements SmartLifecycle{
    private volatile boolean running = false;
    private final DisruptorContainer container;
    private final int phase;

    public DisruptorSupportLifecycle(DisruptorContainer container, int phase) {
        this.container = container;
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
        this.container.publishEvent(DisruptorEvent.START);
    }

    @Override
    public void stop() {
        this.running = false;
        this.container.publishEvent(DisruptorEvent.SHUTDOWN);
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
