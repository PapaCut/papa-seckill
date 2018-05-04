package org.papa.seckill.event.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import org.papa.seckill.RequestDto;

/**
 * Created by PaperCut on 2018/2/26.
 * 请求事件生产者.用于发布事件到disruptor
 */
public class RequestProducer {
    private final EventTranslatorOneArg<RequestEvent, RequestDto> TRANSLATOR =
            (event, seq, request) -> event.setRequestDto(request);

    private final RingBuffer<RequestDto> ringBuffer;

    public RequestProducer(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(RequestDto requestDto) {
        ringBuffer.publishEvent((EventTranslatorOneArg) TRANSLATOR, requestDto);
    }
}
