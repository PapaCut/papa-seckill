package org.papa.seckill.jms.sender.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class PayloadEventProducer {
    private static final EventTranslatorOneArg<PayloadEvent, Object> TRANSLATOR =
            (event, seq, payload) -> event.setPayload(payload);

    private final RingBuffer ringBuffer;
    public PayloadEventProducer(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(Object payload) {
        ringBuffer.publishEvent(TRANSLATOR, payload);
    }
}
