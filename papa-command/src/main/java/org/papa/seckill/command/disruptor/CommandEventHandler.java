package org.papa.seckill.command.disruptor;

import com.lmax.disruptor.EventHandler;
import org.papa.seckill.command.CommandEvent;
import org.papa.seckill.command.CommandExecutor;
import org.papa.seckill.command.CommandBuffer;

/**
 * Created by PaperCut on 2018/2/17.
 */
public class CommandEventHandler implements EventHandler<CommandEvent> {
    private final CommandBuffer buffer;
    private final CommandExecutor executor;

    public CommandEventHandler(CommandBuffer buffer, CommandExecutor executor) {
        this.buffer = buffer;
        this.executor = executor;
    }

    @Override
    public void onEvent(CommandEvent event, long l, boolean endOfBatch) throws Exception {
        if (!buffer.hasRemaining()) {
            flushBuffer();
        }

        buffer.put(event.getCommand());
        if (endOfBatch) {
            flushBuffer();
        }
    }

    /**
     * 刷新缓冲区
     */
    private void flushBuffer() {
        executor.execute(buffer);
        buffer.clear();
    }
}