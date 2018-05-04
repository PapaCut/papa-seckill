package org.papa.metrics.spring;

import com.codahale.metrics.ScheduledReporter;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.TimeUnit;

/**
 * Created by PaperCut on 2018/4/4.
 */
public class ConsoleMetricsLifecycle implements SmartLifecycle {
    private final ScheduledReporter reporter;
    private volatile boolean running = false;
    private final int phase;

    public ConsoleMetricsLifecycle(ScheduledReporter reporter, int phase) {
        this.reporter = reporter;
        this.phase = phase;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        runnable.run();
        this.stop();
    }

    @Override
    public void start() {
        this.running = true;
        this.reporter.start(1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        this.running = false;
        this.reporter.stop();
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
