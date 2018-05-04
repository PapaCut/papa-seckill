package org.papa.seckill.command;

/**
 * Created by PaperCut on 2018/2/27.
 */
public interface CommandBus {
    void subscribe(Class<? extends Command> cmdClass, CommandChannel channel);

    void dispatch(Command command);
}
