package org.papa.common.unitofwork;

/**
 * Created by PaperCut on 2017/11/17.
 */
public class ExecutionException extends RuntimeException {
    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
