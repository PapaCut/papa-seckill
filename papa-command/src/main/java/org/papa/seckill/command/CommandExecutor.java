package org.papa.seckill.command;

/**
 * Created by PaperCut on 2018/2/14.
 */
public interface CommandExecutor<T extends Command> {
    void execute(CommandBuffer<T> buffer);
}
