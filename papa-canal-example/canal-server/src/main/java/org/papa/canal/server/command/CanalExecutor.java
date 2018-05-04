package org.papa.canal.server.command;

import org.papa.canal.EventType;
import org.papa.canal.server.redis.RedisService;
import org.papa.seckill.command.CommandBuffer;
import org.papa.seckill.command.CommandExecutor;
import org.papa.seckill.command.annotation.CommandHandler;
import org.papa.seckill.command.disruptor.DisruptorCommandChannel;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/3/4.
 * Canal命令执行器(缓存更新操作)
 */

@CommandHandler(command = CanalCommand.class, channel = DisruptorCommandChannel.class)
@Component
public class CanalExecutor implements CommandExecutor<CanalCommand> {
    private final RedisService redisService;

    public CanalExecutor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void execute(CommandBuffer<CanalCommand> buffer) {
        for(CanalCommand command : buffer.get()) {
            String key = command.getKey();
            EventType eventType = command.getEventType();
            String field = command.getField();
            Object value = command.getValue();
            try {
                if (eventType == EventType.DELETE) {
                    redisService.hdel(key, field);
                } else if (eventType == EventType.UPDATE || eventType == EventType.INSERT) {
                    redisService.hset(key, field, value.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
