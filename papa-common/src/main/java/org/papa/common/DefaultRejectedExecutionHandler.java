package org.papa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by PaperCut on 2018/5/9.
 */
public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultRejectedExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if(logger.isWarnEnabled()) {
            logger.warn("Out of thread pool.");
        }
        throw new RejectedExecutionException();
    }
}
