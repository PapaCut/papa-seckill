package org.papa.seckill.command;

/**
 * Created by PaperCut on 2018/2/14.
 */
public class CommandEvent<T extends Command> {
    private T command;

    public void setCommand(T command) {
        this.command = command;
    }

    public T getCommand() {
        return command;
    }

    public void clearForGc() {
        this.command = null;
    }
}
