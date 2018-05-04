package org.papa.common.unitofwork;

import org.papa.common.Message;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Created by PaperCut on 2017/11/16.
 */
public class DefaultUnitOfWork<T extends Message> extends AbstractUnitOfWork<T>{
    private MessageProcessingContext<T> processingContext;

    public DefaultUnitOfWork(T message) {
        processingContext = new MessageProcessingContext(message);
    }

    public static <T extends Message> DefaultUnitOfWork<T> startAndGet(T message)
    {
        DefaultUnitOfWork<T> uow = new DefaultUnitOfWork<T>(message);
        uow.start();
        return uow;
    }

    @Override
    public <R> R executeWithResult(Callable<R> task, RollbackConfiguration rollbackConfiguration) throws Exception {
        if (phase() == Phase.NOT_STARTED)
        {
            start();
        }

        R result;
        try
        {
            changePhase(Phase.PREPARE_COMMIT);
            result = task.call();
        }
        catch(Error | Exception e)
        {
            if(rollbackConfiguration.rollbackOn(e.getCause()))
            {
                rollback();
            }
            else
            {
                setExecutionResult(new ExecutionResult(e));
                commit();
            }
            throw e;
        }
        setExecutionResult(new ExecutionResult(result));
        commit();
        return result;
    }

    @Override
    public T getMessage() {
        return processingContext.getMessage();
    }

    @Override
    public ExecutionResult getExecutionResult() {
        return processingContext.getExecutionResult();
    }

    @Override
    protected void setExecutionResult(ExecutionResult executionResult) {
        processingContext.setExecutionResult(executionResult);
    }

    @Override
    protected void addHandler(Phase phase, Consumer<UnitOfWork<T>> handler) {
        processingContext.addHandler(phase, handler);
    }

    @Override
    protected void notifyHandlers(Phase phase) {
        processingContext.notifyHandlers(this, phase);
    }

    @Override
    protected void setRollbackCause(Throwable cause) {
        setExecutionResult(new ExecutionResult(cause));
    }
}
