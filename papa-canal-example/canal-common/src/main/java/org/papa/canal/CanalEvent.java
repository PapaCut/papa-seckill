package org.papa.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

/**
 * Created by PaperCut on 2018/2/6.
 */
public class CanalEvent {
    private String tableName;
    private Object id;
    private List<CanalEntry.Column> beforeColumnsList;
    private List<CanalEntry.Column> afterColumnsList;
    private CanalEntry.EventType eventType;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public List<CanalEntry.Column> getBeforeColumnsList() {
        return beforeColumnsList;
    }

    public void setBeforeColumnsList(List<CanalEntry.Column> beforeColumnsList) {
        this.beforeColumnsList = beforeColumnsList;
    }

    public List<CanalEntry.Column> getAfterColumnsList() {
        return afterColumnsList;
    }

    public void setAfterColumnsList(List<CanalEntry.Column> afterColumnsList) {
        this.afterColumnsList = afterColumnsList;
    }

    public CanalEntry.EventType getEventType() {
        return eventType;
    }

    public void setEventType(CanalEntry.EventType eventType) {
        this.eventType = eventType;
    }
}
