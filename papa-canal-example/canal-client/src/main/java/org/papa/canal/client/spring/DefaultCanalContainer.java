package org.papa.canal.client.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.Sets;
import com.google.protobuf.InvalidProtocolBufferException;
import org.papa.canal.CanalEvent;
import org.papa.canal.client.Registration;
import org.papa.canal.client.SubscribableCanalEvent;
import org.papa.canal.client.config.CanalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/2/6.
 */
public class DefaultCanalContainer extends AbstractCanalContainer implements SubscribableCanalEvent<CanalEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultCanalContainer.class);

    private final Set<Consumer<CanalEvent>> consumers = Sets.newHashSet();

    public DefaultCanalContainer(String name, int phase, CanalProperties config) {
        super(name, phase, config);
    }

    @Override
    protected void doHandle(List<CanalEntry.Entry> entrys) {
        for(CanalEntry.Entry entry : entrys) {
            String tableName = entry.getHeader().getTableName();

            if(entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() ==  CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            CanalEntry.RowChange rowChange = null;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException("Error parser of eromanga-event has an error");
            }

            for(CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                if(CollectionUtils.isEmpty(rowData.getAfterColumnsList())){
                    throw new RuntimeException("The rowData.getAfterColumnsList is empty..");
                }
                String id = rowData.getAfterColumns(0).getValue();
                final CanalEntry.EventType eventType = rowChange.getEventType();
                CanalEvent event = new CanalEvent();
                event.setId(id);
                event.setTableName(tableName);
                event.setEventType(eventType);
                event.setBeforeColumnsList(rowData.getBeforeColumnsList());
                event.setAfterColumnsList(rowData.getAfterColumnsList());

                this.publish(event);

                if(logger.isDebugEnabled()) {
                    logger.debug("Publish msg: {}", JSON.toJSONString(event));
                }
            }
        }
    }

    protected void publish(CanalEvent event) {
        getConsumers().forEach((consumer) -> consumer.accept(event));
    }

    protected Set<Consumer<CanalEvent>> getConsumers() {
        return this.consumers;
    }

    @Override
    public Registration subscribe(Consumer<CanalEvent> consumer) {
        getConsumers().add(consumer);
        return () -> getConsumers().remove(consumer);
    }
}
