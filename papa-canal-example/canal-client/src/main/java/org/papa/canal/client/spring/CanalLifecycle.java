package org.papa.canal.client.spring;

import org.springframework.context.SmartLifecycle;

/**
 * Created by PaperCut on 2018/2/6.
 */
public abstract class CanalLifecycle implements SmartLifecycle {
    private final String name;
    protected volatile boolean running = false;
    private int phase;

    public CanalLifecycle(String name, int phase) {
        this.name = name;
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
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return phase;
    }
}
