package org.papa.canal.server.event;

import com.alibaba.fastjson.JSON;
import org.papa.canal.CacheMessage;
import org.papa.canal.server.command.CanalCommand;
import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestHandler;
import org.papa.seckill.ResponseDto;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PaperCut on 2018/3/4.
 */
public class CanalHandler implements RequestHandler<CacheMessage> {
    private static final Logger logger = LoggerFactory.getLogger(CanalHandler.class);

    @Override
    public RequestEvent<CacheMessage> onEvent(RequestEvent<CacheMessage> event) {
        RequestDto<CacheMessage> request = event.getRequestDto();

        CacheMessage message = request.getPayload();

        // 新建更新缓存命令
        CanalCommand command = new CanalCommand(request.getId());
        command.setKey(message.getCacheKey());
        command.setField(message.getCacheField());
        command.setValue(message.getCacheValue());
        command.setEventType(message.getEventType());

        if(logger.isDebugEnabled()) {
            logger.debug("Build commnad: {}", JSON.toJSONString(command));
        }

        // 加入命令缓冲区
        event.getCommandCollector().add(command);

        // 生成response
        ResponseDto response = new ResponseDto(request.getId());
        response.setSuccess(true);
        event.setResponseDto(response);

        return event;
    }
}
