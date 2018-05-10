package org.papa.common;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by PaperCut on 2018/5/9.
 */
public class DefaultThreadPool implements ThreadPool{
    private int corePoolSize;
    private int maximumPoolSize;
    private int keepAliveTime;
    private int queueSize;
    private boolean allowCoreThreadTimeout;
    private boolean prestartAllCoreThreads;
    transient volatile ThreadPoolExecutor executor;

    public DefaultThreadPool(Builder builder) {
        this.corePoolSize = builder.corePoolSize;
        this.maximumPoolSize = builder.maximumPoolSize;
        this.keepAliveTime = builder.keepAliveTime;
        this.queueSize = builder.queueSize;
        this.allowCoreThreadTimeout = builder.allowCoreThreadTimeout;
        this.prestartAllCoreThreads = builder.prestartAllCoreThreads;
    }

    /**
     * 初始化线程池
     */
    @Override
    public void init() {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize), new DefaultThreadFactory("default-thread-pool"));
        // 销毁所有超过keepAliveTime的线程
        if(allowCoreThreadTimeout) {
            executor.allowCoreThreadTimeOut(true);
        }

        // 初始化所有核心线程
        if(prestartAllCoreThreads) {
            executor.prestartAllCoreThreads();
        }
    }

    /**
     * 销毁线程池
     */
    @Override
    public void destroy() {
        if(executor != null) {
            executor.shutdown();
        }
    }

    @Override
    public ThreadPoolExecutor getExecutor() {
        // 延迟懒加载线程池
        if(executor == null) {
            synchronized (this) {
                if(executor == null) {
                    init();
                }
            }
        }
        return executor;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public boolean isAllowCoreThreadTimeout() {
        return allowCoreThreadTimeout;
    }

    public boolean isPrestartAllCoreThreads() {
        return prestartAllCoreThreads;
    }

    public static class Builder {
        private int corePoolSize = 20;
        private int maximumPoolSize = 100;
        private int keepAliveTime = 120000;
        private int queueSize = 0;
        private boolean allowCoreThreadTimeout;
        private boolean prestartAllCoreThreads;

        public Builder setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        public Builder setKeepAliveTime(int keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        public Builder setQueueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        public Builder setAllowCoreThreadTimeout(boolean allowCoreThreadTimeout) {
            this.allowCoreThreadTimeout = allowCoreThreadTimeout;
            return this;
        }

        public Builder setPrestartAllCoreThreads(boolean prestartAllCoreThreads) {
            this.prestartAllCoreThreads = prestartAllCoreThreads;
            return this;
        }

        public DefaultThreadPool build() {
            return new DefaultThreadPool(this);
        }
    }
}
