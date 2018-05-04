package org.papa.seckill.command;

import java.util.List;

/**
 * Created by PaperCut on 2018/2/14.
 */
public interface CommandBuffer<T extends Command>{
    void put(T command);

    void clear();

    boolean hasRemaining();

    List<T> get();

    int size();
}
