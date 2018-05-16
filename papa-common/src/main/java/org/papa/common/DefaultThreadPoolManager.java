package org.papa.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by PaperCut on 2018/5/10.
 */
public class DefaultThreadPoolManager {
    private static ConcurrentMap<String, ThreadPool> poolsMap = null;

    public static synchronized ThreadPool registerThreadPool(String name, ThreadPool threadPool) {
        if(poolsMap == null) {
            poolsMap = new ConcurrentHashMap();
        }
        return poolsMap.put(name, threadPool);
    }

    public static synchronized void unRegisterThreadPool(String name) {
        if(poolsMap != null) {
            poolsMap.remove(name);
        }
    }

    public static ThreadPool getThreadPool(String name) {
        if(poolsMap != null) {
            return poolsMap.get(name);
        }
        return null;
    }
}
