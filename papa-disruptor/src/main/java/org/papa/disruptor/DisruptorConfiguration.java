package org.papa.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PaperCut on 2018/3/2.
 * Disruptor配置
 */
public class DisruptorConfiguration {
    public static final int DEFAULT_BUFFER_SIZE = 1024;    // 默认的disruptor buffer大小

    private ExecutorService executorService;
    private int bufferSize = DEFAULT_BUFFER_SIZE;
    private WaitStrategy waitStrategy = new BlockingWaitStrategy();
    private ProducerType producerType = ProducerType.MULTI;

    public DisruptorConfiguration() { }

    public DisruptorConfiguration(DisruptorConfiguration configuration) {
        this.executorService = configuration.executorService;
        this.bufferSize = configuration.bufferSize;
        this.waitStrategy = configuration.waitStrategy;
        this.producerType = configuration.producerType;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public WaitStrategy getWaitStrategy() {
        return waitStrategy;
    }

    public ProducerType getProducerType() {
        return producerType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DisruptorConfiguration target;

        public Builder() {
            this.target = new DisruptorConfiguration();
        }

        public Builder bufferSize(int bufferSize) {
            target.bufferSize = bufferSize;
            return this;
        }

        public Builder waitStrategy(WaitStrategy waitStrategy) {
            target.waitStrategy = waitStrategy;
            return this;
        }

        public Builder producerType(ProducerType producerType) {
            target.producerType = producerType;
            return this;
        }

        public Builder executorService(ExecutorService executorService) {
            target.executorService = executorService;
            return this;
        }

        public DisruptorConfiguration build() {
            return new DisruptorConfiguration(target);
        }
    }
}
