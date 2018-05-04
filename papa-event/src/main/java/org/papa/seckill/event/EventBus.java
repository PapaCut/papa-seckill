package org.papa.seckill.event;

import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestProcessor;

/**
 * Created by PaperCut on 2018/2/27.
 * 事件总线
 */
public interface EventBus {
    void publish(RequestDto requestDto);

    void subscribe(String topic, RequestProcessor processor);
}
