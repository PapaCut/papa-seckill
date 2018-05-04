package org.papa.metrics.command;

/**
 * Created by PaperCut on 2018/3/17.
 */
public enum NoCommandMonitor implements CommandMonitor {
    INSTANCE;

    @Override
    public void incrCmd() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure() {

    }
}
