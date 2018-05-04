package org.papa.seckill.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PaperCut on 2018/2/26.
 * 命令收集器
 */
public class CommandCollector {
    private final List<Command> commands = new ArrayList<>(16);

    public List<Command> getCommands() {
        return new ArrayList<>(commands);
    }

    public void add(Command command) {
        this.commands.add(command);
    }
}
