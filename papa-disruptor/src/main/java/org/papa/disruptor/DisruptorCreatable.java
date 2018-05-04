package org.papa.disruptor;

import com.lmax.disruptor.dsl.Disruptor;

import java.util.function.Supplier;

/**
 * Created by PaperCut on 2018/2/27.
 */
public interface DisruptorCreatable {
    Supplier<Disruptor> createDisruptor();
}
