package org.papa.canal.client.spring;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.papa.canal.client.config.CanalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by PaperCut on 2018/2/6.
 */
public abstract class AbstractCanalContainer extends CanalLifecycle implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCanalContainer.class);

    private final CanalProperties config;
    private final CanalConnector connector;

    private final int totalEmptyCount = 1200;
    private final int batchSize = 0;
    private final LongAdder emptyCount = new LongAdder();

    public AbstractCanalContainer(String name, int phase, CanalProperties config) {
        super(name, phase);

        Assert.notNull(config, "Canal config can't be null");
        this.config = config;
        this.connector = CanalConnectors.newSingleConnector(new InetSocketAddress(config.getHost(), config.getPort()),
                config.getDestination(), config.getUsername(), config.getPassword());
    }

    @Override
    public void start(){
        this.doStart();
    }

    @Override
    public void stop(){
        this.doStop();
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }

    private void doReceive() throws InterruptedException {
        while(emptyCount.longValue() < totalEmptyCount){
            Message message = connector.getWithoutAck(batchSize);
            long batchId = message.getId();
            int size = message.getEntries().size();
            if(batchId == -1 || size == 0) {
                emptyCount.increment();
                if(logger.isDebugEnabled()) {
                    logger.debug("empty count: {}", emptyCount.longValue());
                }
                Thread.sleep(1000);
            } else {
                emptyCount.reset();
                doHandle(message.getEntries());
            }
            connector.ack(batchId);
        }
    }

    private void doStart() {
        connector.connect();
        connector.subscribe(".*\\..*");
        connector.rollback();
        try {
            Thread thread = new Thread(() -> {
                try {
                    this.doReceive();
                } catch (InterruptedException e) {
                    logger.error("Failed to start CanalContainer.Cause by {}", e.getMessage());
                    this.stop();
                }
            });
            thread.start();

            this.running = true;
        } catch (Exception e) {
            logger.error("Failed to start CanalContainer.Cause by {}", e.getMessage());
            this.stop();
        }
    }

    private void doStop() {
        connector.disconnect();
        this.running = false;
    }

    protected abstract void doHandle(List<CanalEntry.Entry> entrys);
}
