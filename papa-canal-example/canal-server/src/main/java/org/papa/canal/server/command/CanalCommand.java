package org.papa.canal.server.command;

import org.papa.canal.EventType;
import org.papa.seckill.command.Command;

/**
 * Created by PaperCut on 2018/3/4.
 * Canal更新命令
 */
public class CanalCommand extends Command{
    private String key;
    private String field;
    private Object value;
    private EventType eventType;

    public CanalCommand(String requestId) {
        super(requestId);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
