package org.papa.seckill.command;

import com.google.common.collect.Maps;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by PaperCut on 2018/2/19.
 * 命令缓冲区
 */
public class DefaultCommandBuffer<T extends Command> implements CommandBuffer<T> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultCommandBuffer.class);

    private static final int DEFAULT_CAPACITY = 16;

    private final Map<String, T> commandMap = Maps.newConcurrentMap();
    private final int capacity;

    public DefaultCommandBuffer() {
        this.capacity = DEFAULT_CAPACITY;
    }
    public DefaultCommandBuffer(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void put(T command) {
        if(!hasRemaining()) {
            logger.error("Overflow command buffer..");
            throw new CommandBufferOverflowException();
        }

        String key = command.getId();
        if(commandMap.get(key) != null) {
            logger.warn("The commandMap already has this command. The itemId: {}", key);
        } else {
            commandMap.put(key, command);
            logger.info("Succeed put command into commandMap. The itemId: {}", key);
        }
    }

    @Override
    public void clear() {
        commandMap.clear();
    }

    @Override
    public boolean hasRemaining() {
        return commandMap.size() < capacity;
    }

    @Override
    public List<T> get() {
        return new ArrayList<>(commandMap.values());
    }

    @Override
    public int size() {
        return commandMap.size();
    }
}
