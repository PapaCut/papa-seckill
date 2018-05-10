package org.papa.seckill.command.normal;

import org.papa.seckill.command.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/2/25.
 * Disruptor有批量、我也要有..
 */
public class MergeCommandChannel implements CommandChannel {
    private final CommandBuffer commandBuffer;
    private Thread commandFlusher;
    private volatile boolean running = false;
    private CommandExecutor commandExecutor;

    public MergeCommandChannel(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        this.commandBuffer = new DefaultCommandBuffer(16);
    }

    @Override
    public Consumer<Command> getConsumer() {
        return (cmd) -> {
            commandBuffer.put(cmd);
            if(running) {
                flush();
            } else {
                running = true;
                commandFlusher = new Thread(new CommandFlusher());
                commandFlusher.start();
            }
        };
    }

    private void flush() {
        if(!commandBuffer.hasRemaining()) {
            LockSupport.unpark(commandFlusher);

        }
    }

    public void doExecute() {
        if(commandBuffer.size() > 0) {
            commandExecutor.execute(commandBuffer);
            commandBuffer.clear();
        }
    }

    private void doStart() {
        LockSupport.parkNanos(commandFlusher, TimeUnit.MILLISECONDS.toNanos(100));
        this.running = false;
        doExecute();
    }

    private class CommandFlusher implements Runnable {
        @Override
        public void run() {
            doStart();
        }
    }
}
