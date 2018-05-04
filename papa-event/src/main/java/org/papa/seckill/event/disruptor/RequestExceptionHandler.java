package org.papa.seckill.event.disruptor;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.ExceptionHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PaperCut on 2018/2/26.
 * 请求异常处理器
 */
public class RequestExceptionHandler implements ExceptionHandler<RequestEvent>{
    private static final Logger logger = LoggerFactory.getLogger(RequestExceptionHandler.class);

    @Override
    public void handleEventException(Throwable throwable, long l, RequestEvent requestEvent) {
        logger.error("Failed to process request event. event:{}, msg:{}",
                JSON.toJSONString(requestEvent), ExceptionUtils.getMessage(throwable));
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        logger.error("Failed to start the request handler.", throwable);
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        logger.error("Failed to shutdown the request handler.", throwable);
    }
}
