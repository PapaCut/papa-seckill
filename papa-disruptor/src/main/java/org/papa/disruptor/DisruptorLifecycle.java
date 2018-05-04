package org.papa.disruptor;

/**
 * Created by PaperCut on 2018/3/26.
 */
public interface DisruptorLifecycle extends DisruptorEventListener {
    void start();
    void stop();

    String getDisruptorName();
}
