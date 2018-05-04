package org.papa.common.unitofwork;

/**
 * Created by PaperCut on 2017/11/16.
 */
public class ExecutionResult {
    private final Object result;

    public ExecutionResult(Object result) {
        this.result = result;
    }

    public Object getResult()
    {
        if(isExceptionResult())
        {
            if(result instanceof RuntimeException)
            {
                throw (RuntimeException)result;
            }
            if(result instanceof Error)
            {
                throw (Error)result;
            }
            throw new ExecutionException("", (Throwable)result);
        }
        return result;
    }

    public Throwable getExceptionResult()
    {
        return isExceptionResult()?(Throwable)result:null;
    }

    public boolean isExceptionResult()
    {
        return result instanceof Throwable;
    }
}
