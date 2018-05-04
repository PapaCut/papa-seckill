package org.papa.seckill.command.exception;

/**
 * Created by PaperCut on 2018/3/26.
 */
public class ChannelNotFoundException extends RuntimeException{
    public ChannelNotFoundException() {
    }

    public ChannelNotFoundException(String message) {
        super(message);
    }

    public ChannelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelNotFoundException(Throwable cause) {
        super(cause);
    }

    public ChannelNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
