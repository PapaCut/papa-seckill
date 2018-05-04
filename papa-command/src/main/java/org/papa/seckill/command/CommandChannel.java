package org.papa.seckill.command;

import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/2/25.
 */
public interface CommandChannel {
    Consumer<Command> getConsumer();
}
