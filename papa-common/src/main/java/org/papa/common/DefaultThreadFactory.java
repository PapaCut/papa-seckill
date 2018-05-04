package org.papa.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by PaperCut on 2018/5/1.
 */
public class DefaultThreadFactory implements ThreadFactory {
    private final AtomicInteger threadCount = new AtomicInteger(1);

    private final String prefix;
    private final boolean isDaemon;

    public DefaultThreadFactory(String prefix) {
        this(prefix, false);
    }

    public DefaultThreadFactory(String prefix, boolean isDaemon) {
        this.prefix = prefix;
        this.isDaemon = isDaemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, prefix + "-" + threadCount.incrementAndGet());
        thread.setDaemon(isDaemon);
        return thread;
    }
}
