package org.papa.seckill.command.disruptor;

import com.lmax.disruptor.EventHandler;
import org.papa.seckill.command.CommandEvent;

/**
 * Created by PaperCut on 2018/2/27.
 */
public class CommandGcHandler implements EventHandler<CommandEvent>{
    @Override
    public void onEvent(CommandEvent commandEvent, long l, boolean b) throws Exception {
        commandEvent.clearForGc();
    }
}
