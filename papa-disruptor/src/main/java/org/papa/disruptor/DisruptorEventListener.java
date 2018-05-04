package org.papa.disruptor;

/**
 * Created by PaperCut on 2018/3/26.
 */
public interface DisruptorEventListener {
    void onEvent(DisruptorEvent event);
}
