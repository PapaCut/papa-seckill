package org.papa.disruptor;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by PaperCut on 2018/2/27.
 */
public abstract class AbstractDisruptorLifecycle implements DisruptorCreatable, DisruptorLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDisruptorLifecycle.class);

    protected Disruptor disruptor;
    protected DisruptorConfiguration configuration;
    protected ExecutorService executorService;
    private final String disruptorName;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public AbstractDisruptorLifecycle(String disruptorName, DisruptorConfiguration configuration) {
        this.disruptorName = disruptorName;
        this.configuration = configuration;
        this.executorService = Optional.ofNullable(configuration.getExecutorService())
                .orElse(Executors.newCachedThreadPool());
    }

    @Override
    public void start() {
        if(running.compareAndSet(false, true)) {
            this.disruptor = createDisruptor().get();
            this.disruptor.start();
            logger.info("Starting {} disruptor...", disruptorName);
            doStart();
        } else {
            throw new IllegalStateException("The disruptor " + disruptorName + " already started.");
        }
    }

    @Override
    public void stop() {
        if(running.compareAndSet(true, false)) {
            this.disruptor.shutdown();
            executorService.shutdown();
            logger.info("Shutdown {} disruptor...", disruptorName);
            doStop();
        } else {
            throw new IllegalStateException("The disruptor " + disruptorName + " is not started.");
        }
    }

    @Override
    public void onEvent(DisruptorEvent event) {
        if(event == DisruptorEvent.START) {
            this.start();
        } else if (event == DisruptorEvent.SHUTDOWN) {
            this.stop();
        } else {
            throw new IllegalArgumentException("Unrecognized disruptor event. Event:" + JSON.toJSONString(event));
        }
    }

    protected ExecutorService getExecutorService() {
        return executorService;
    }

    public DisruptorConfiguration getConfiguration() {
        return configuration;
    }

    protected abstract void doStart();

    protected abstract void doStop();

    protected Disruptor getDisruptor() {
        return disruptor;
    }

    @Override
    public String getDisruptorName() {
        return disruptorName;
    }
}
