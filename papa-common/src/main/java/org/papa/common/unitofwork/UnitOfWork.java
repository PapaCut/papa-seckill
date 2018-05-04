package org.papa.common.unitofwork;

import org.papa.common.Message;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by PaperCut on 2017/11/16.
 */
public interface UnitOfWork<T extends Message> {
    void start();
    void commit();
    default void rollback()
    {
        rollback(null);
    }

    void rollback(Throwable cause);
    default <R> R executeWithResult(Callable<R> task) throws Exception {
        return executeWithResult(task, RollbackConfigurationType.ANY_THROWABLE);
    }

    <R> R executeWithResult(Callable<R> task, RollbackConfiguration rollbackConfiguration) throws Exception;
    T getMessage();

    boolean isRolledback();

    ExecutionResult getExecutionResult();

    default boolean isCurrent()
    {
        return CurrentUnitOfWork.isStarted() && CurrentUnitOfWork.get() == this;
    }
    default boolean isActive()
    {
        return phase().isStarted();
    }
    default boolean isRoot()
    {
        return !parent().isPresent();
    }

    Map<String, Object> resources();

    default UnitOfWork<T> root()
    {
        return parent().map(UnitOfWork::root).orElse((UnitOfWork)this);
    }

    default <R> R getResources(String name)
    {
        return (R)resources().get(name);
    }

    default <R> R getOrComputeResource(String key, Function<? super String, R> mappingFunction)
    {
        return (R)resources().computeIfAbsent(key, mappingFunction);
    }

    default <R> R getOrDefaultResource(String key, R defaultValue)
    {
        return (R)resources().getOrDefault(key, defaultValue);
    }

    default void execute(Runnable task)
    {
        try
        {
            executeWithResult(() -> {
                task.run();
                return null;
            });
        }
        catch(Exception e)
        {
            throw (RuntimeException)e;
        }
    }

    default void execute(Runnable task, RollbackConfiguration rollbackConfiguration)
    {
        try
        {
            executeWithResult(() -> {
                task.run();
                return null;
            }, rollbackConfiguration);
        }
        catch(Exception e)
        {
            throw (RuntimeException)e;
        }
    }

    Optional<UnitOfWork<?>> parent();


    Phase phase();

    void onStart(Consumer<UnitOfWork<T>> handler);
    void onPrepareCommit(Consumer<UnitOfWork<T>> handler);
    void onCommit(Consumer<UnitOfWork<T>> handler);
    void onAfterCommit(Consumer<UnitOfWork<T>> handler);
    void onRollback(Consumer<UnitOfWork<T>> handler);
    void onCleanup(Consumer<UnitOfWork<T>> handler);
    void onClose(Consumer<UnitOfWork<T>> handler);


    enum Phase
    {
        NOT_STARTED(false, false),
        STARTED(true, false),
        PREPARE_COMMIT(true, false),
        COMMIT(true, false),
        ROLLBACK(true, true),
        AFTER_COMMIT(true, true),
        CLEAN_UP(false, true),
        CLOSED(false, true)
        ;
        private final boolean started;
        private final boolean reverseCallbackOrder;

        Phase(boolean started, boolean reverseCallbackOrder) {
            this.started = started;
            this.reverseCallbackOrder = reverseCallbackOrder;
        }


        public boolean isReverseCallbackOrder() {
            return reverseCallbackOrder;
        }

        public boolean isStarted() {
            return started;
        }

        public boolean isBefore(Phase phase)
        {
            return ordinal() < phase.ordinal();
        }

        public boolean isAfter(Phase phase)
        {
            return ordinal() > phase.ordinal();
        }
    }
}
