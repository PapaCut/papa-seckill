package org.papa.seckill;

/**
 * Created by PaperCut on 2018/2/26.
 * 请求传输实体对象
 */
public class RequestDto<T> extends MessageDto {
    private T payload;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
