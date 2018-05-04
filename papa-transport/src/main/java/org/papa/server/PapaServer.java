package org.papa.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by PaperCut on 2018/4/23.
 */
public abstract class PapaServer {
    private static final Logger logger = LoggerFactory.getLogger(PapaServer.class);

    private AtomicBoolean started = new AtomicBoolean(false);
    private AtomicBoolean inited = new AtomicBoolean(false);

    protected final String bindAddr;
    protected final int port;

    public PapaServer(String bindAddr, int port) {
        this.bindAddr = bindAddr;
        this.port = port;
    }

    public void init() {
        if(inited.compareAndSet(false, true)) {
            logger.warn("Initialize the server.");
            this.doInit();
        } else {
            logger.warn("Server has been inited already.");
        }
    }

    public void stop() {
        if(started.compareAndSet(true, false)) {
            this.doStop();
        } else {
            throw new IllegalStateException("Error: The server has already stopped!");
        }
    }

    public boolean start() {
        this.init();
        if(started.compareAndSet(false, true)) {
            try {
                logger.warn("Server starting..");
                return this.doStart();
            } catch(Throwable t) {
                started.set(false);
                logger.error("Error: Failed to start the server.");
                return false;
            }
        } else {
            logger.error("The server has already started.");
            return false;
        }
    }

    protected abstract void doInit();

    protected abstract boolean doStart() throws InterruptedException;

    protected abstract void doStop();

}
