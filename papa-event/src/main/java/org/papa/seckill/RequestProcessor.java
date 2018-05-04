package org.papa.seckill;

import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/2/26.
 * 请求处理器接口
 */
public interface RequestProcessor {
    Consumer<RequestDto> getConsumer();
}
