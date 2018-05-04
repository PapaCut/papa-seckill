package org.papa.metrics.command;

/**
 * Created by PaperCut on 2018/3/17.
 */
public interface CommandMonitor {
    void incrCmd();

    void onSuccess();
    void onFailure();
}
