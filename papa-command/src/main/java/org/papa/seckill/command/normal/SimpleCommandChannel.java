package org.papa.seckill.command.normal;


import org.papa.seckill.command.*;

import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class SimpleCommandChannel implements CommandChannel {
    private CommandBuffer commandBuffer;
    private final CommandExecutor commandExecutor;

    public SimpleCommandChannel(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        commandBuffer = new DefaultCommandBuffer(16);
    }

    @Override
    public Consumer<Command> getConsumer() {
        return (cmd) -> {
            commandBuffer.put(cmd);
            commandExecutor.execute(commandBuffer);
            commandBuffer.clear();
        };
    }
}
