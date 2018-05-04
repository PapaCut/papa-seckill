package org.papa.metrics.command;

import com.codahale.metrics.Counter;

/**
 * Created by PaperCut on 2018/5/1.
 */
public class SimpleCommandMonitor implements CommandMonitor{
    private final Counter successCounter = new Counter();
    private final Counter failureCounter = new Counter();
    private final Counter ingestedCounter = new Counter();

    @Override
    public void incrCmd() {
        ingestedCounter.inc();
    }

    @Override
    public void onSuccess() {
        successCounter.inc();
    }

    @Override
    public void onFailure() {
        failureCounter.inc();
    }
}
