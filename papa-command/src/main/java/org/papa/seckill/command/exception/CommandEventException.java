package org.papa.seckill.command.exception;

/**
 * Created by PaperCut on 2018/3/26.
 */
public class CommandEventException extends RuntimeException{
    public CommandEventException() {
    }

    public CommandEventException(String message) {
        super(message);
    }

    public CommandEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandEventException(Throwable cause) {
        super(cause);
    }

    public CommandEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
