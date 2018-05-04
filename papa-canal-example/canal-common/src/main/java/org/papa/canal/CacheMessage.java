package org.papa.canal;

import java.io.Serializable;

/**
 * Created by PaperCut on 2018/2/6.
 */
public class CacheMessage implements Serializable{
    private String msgId;
    private EventType eventType;
    private String cacheKey;
    private String cacheField;
    private Object cacheValue;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheField() {
        return cacheField;
    }

    public void setCacheField(String cacheField) {
        this.cacheField = cacheField;
    }

    public Object getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(Object cacheValue) {
        this.cacheValue = cacheValue;
    }
}
