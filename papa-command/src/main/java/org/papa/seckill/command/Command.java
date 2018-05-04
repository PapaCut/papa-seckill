package org.papa.seckill.command;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by PaperCut on 2018/2/14.
 */
public abstract class Command implements Serializable{
    protected final String id;
    protected final String requestId;

    public Command(String requestId) {
        this.id = UUID.randomUUID().toString();
        this.requestId = requestId;
    }

    public String getId() {
        return id;
    }

    public String getRequestId() {
        return requestId;
    }
}
