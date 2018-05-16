package org.papa.seckill.command;

import org.papa.common.DefaultThreadPoolManager;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/5/10.
 */
public class PoolCommandExecutor<T extends Command> implements CommandExecutor<T>{
    private final String executorName;
    private final Consumer handler;

    public PoolCommandExecutor(String executorName, Consumer handler) {
        this.executorName = executorName;
        this.handler = handler;
    }

    @Override
    public void execute(CommandBuffer<T> buffer) {
        ThreadPoolExecutor executor = DefaultThreadPoolManager.getThreadPool(executorName).getExecutor();
        for(T cmd : buffer.get()) {
            executor.execute(() -> {
                handler.accept(cmd);
            });
        }
    }
}
