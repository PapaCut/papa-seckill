package org.papa.seckill;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by PaperCut on 2018/2/26.
 */
public class MessageDto implements Serializable{
    private final String id;

    public MessageDto() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
