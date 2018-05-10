package org.papa.common;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by PaperCut on 2018/5/10.
 */
public interface ThreadPool {
    void init();

    void destroy();

    ThreadPoolExecutor getExecutor();
}
