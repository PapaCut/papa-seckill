package org.papa.seckill.command.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import org.papa.seckill.command.Command;
import org.papa.seckill.command.CommandEvent;

/**
 * Created by PaperCut on 2018/2/14.
 */
public class CommandEventProducer <T extends Command>{
    private final EventTranslatorOneArg<CommandEvent, T> TRANSLATOR =
            (event, seq, cmd) -> event.setCommand(cmd);

    private final RingBuffer<T> ringBuffer;

    public CommandEventProducer(RingBuffer<T> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(T command) {
        ringBuffer.publishEvent((EventTranslatorOneArg) TRANSLATOR, command);
    }
}
