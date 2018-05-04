package org.papa.seckill.command.disruptor;


import com.lmax.disruptor.dsl.Disruptor;
import org.papa.seckill.command.CommandExecutor;

/**
 * Created by PaperCut on 2018/2/19.
 */
public interface DisruptorFactory {
    Disruptor create(CommandExecutor commandExecutor);
}
