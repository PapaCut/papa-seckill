package org.papa.seckill.command;

/**
 * Created by PaperCut on 2018/2/18.
 */
public class CommandBufferOverflowException extends RuntimeException {
    public CommandBufferOverflowException() {
    }

    public CommandBufferOverflowException(String message) {
        super(message);
    }

    public CommandBufferOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandBufferOverflowException(Throwable cause) {
        super(cause);
    }

    public CommandBufferOverflowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
