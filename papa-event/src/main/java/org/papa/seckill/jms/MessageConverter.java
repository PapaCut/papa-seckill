package org.papa.seckill.jms;

import org.papa.seckill.RequestDto;

/**
 * Created by PaperCut on 2018/3/1.
 */
public interface MessageConverter<T extends RequestDto> {
    T readMessage(Object message);

    Object toMessage(T obj);
}
