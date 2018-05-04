package org.papa.disruptor;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by PaperCut on 2018/3/26.
 * Disruptor生命周期的容器
 */
public class DisruptorContainer {
    private final Map<String, DisruptorLifecycle> disruptorLifecycleMap = Maps.newConcurrentMap();

    /**
     * 注册disruptor的生命周期
     * @param disruptorName
     * @param disruptorLifecycle
     */
    public void register(String disruptorName, DisruptorLifecycle disruptorLifecycle) {
        disruptorLifecycleMap.putIfAbsent(disruptorName, disruptorLifecycle);
    }

    /**
     * 发布事件
     * @param event
     */
    public void publishEvent(DisruptorEvent event) {
        disruptorLifecycleMap.forEach((clz, lifecycle) -> lifecycle.onEvent(event));
    }
}
