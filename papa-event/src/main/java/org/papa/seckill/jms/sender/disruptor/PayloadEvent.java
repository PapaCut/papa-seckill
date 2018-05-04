package org.papa.seckill.jms.sender.disruptor;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class PayloadEvent {
    private Object payload;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
