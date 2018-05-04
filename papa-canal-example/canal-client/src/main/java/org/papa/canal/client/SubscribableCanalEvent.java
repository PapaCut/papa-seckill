package org.papa.canal.client;

import org.papa.canal.CanalEvent;

import java.util.function.Consumer;

/**
 * Created by PaperCut on 2018/2/7.
 */
public interface SubscribableCanalEvent<T extends CanalEvent> {
    Registration subscribe(Consumer<T> consumer);
}
