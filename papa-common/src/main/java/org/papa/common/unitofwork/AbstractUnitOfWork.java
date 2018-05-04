package org.papa.common.unitofwork;

import org.papa.common.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by PaperCut on 2017/11/16.
 */
public abstract class AbstractUnitOfWork<T extends Message> implements UnitOfWork<T> {
    private UnitOfWork<?> parentUnitOfWork;
    private Map<String, Object> resources = new HashMap<>();
    private Phase phase = Phase.NOT_STARTED;
    private boolean rolledBack;

    @Override
    public void start() {
        rolledBack = false;
        onRollback(u -> rolledBack = true);
        CurrentUnitOfWork.ifStarted(parent -> {
            this.parentUnitOfWork = parent;
            root().onCleanup(r -> changePhase(Phase.CLEAN_UP, Phase.CLOSED));
        });
        changePhase(Phase.STARTED);
        CurrentUnitOfWork.set(this);
    }

    @Override
    public void commit() {
        try {
            if (isRoot()) {
                commitAsRoot();
            } else {
                commitAsNested();
            }
        }
        finally {
            CurrentUnitOfWork.clear(this);
        }
    }

    private void commitAsRoot()
    {
        try
        {
            try {
                changePhase(Phase.PREPARE_COMMIT, Phase.COMMIT);
            }
            catch(Exception e)
            {
                setRollbackCause(e);
                changePhase(Phase.ROLLBACK);
                throw e;
            }
            if(phase() == Phase.COMMIT)
            {
                changePhase(Phase.AFTER_COMMIT);
            }
        }
        finally {
            changePhase(Phase.CLEAN_UP, Phase.CLOSED);
        }
    }

    private void commitAsNested()
    {
        try
        {
            changePhase(Phase.PREPARE_COMMIT, Phase.COMMIT);
            root().onAfterCommit(u -> changePhase(Phase.AFTER_COMMIT));
            root().onRollback(u -> changePhase(Phase.ROLLBACK));
        }
        catch(Exception e)
        {
            setRollbackCause(e);
            changePhase(Phase.ROLLBACK);
            throw e;
        }
    }

    @Override
    public void rollback(Throwable cause) {
        try
        {
            setRollbackCause(cause);
            changePhase(Phase.ROLLBACK);
            if(isRoot())
            {
                changePhase(Phase.CLEAN_UP, Phase.CLOSED);
            }
        }
        finally {
            CurrentUnitOfWork.clear(this);
        }
    }

    @Override
    public Map<String, Object> resources() {
        return resources;
    }

    @Override
    public boolean isRolledback() {
        return rolledBack;
    }

    @Override
    public Phase phase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    @Override
    public Optional<UnitOfWork<?>> parent() {
        return Optional.ofNullable(parentUnitOfWork);
    }


    protected void changePhase(Phase... phases)
    {
        for(Phase phase : phases)
        {
            setPhase(phase);
            notifyHandlers(phase);
        }
    }

    @Override
    public void onStart(Consumer<UnitOfWork<T>> handler) {
        addHandler(Phase.STARTED, handler);
    }

    @Override
    public void onPrepareCommit(Consumer<UnitOfWork<T>> handler) {
        addHandler(Phase.PREPARE_COMMIT, handler);
    }

    @Override
    public void onCommit(Consumer<UnitOfWork<T>> handler) {
        addHandler(Phase.COMMIT, handler);
    }

    @Override
    public void onAfterCommit(Consumer<UnitOfWork<T>> handler) {
        addHandler(Phase.AFTER_COMMIT, handler);
    }

    @Override
    public void onRollback(Consumer<UnitOfWork<T>> handler) {
        addHandler(Phase.ROLLBACK, handler);
    }

    @Override
    public void onCleanup(Consumer<UnitOfWork<T>> handler) {
        addHandler(Phase.CLEAN_UP, handler);
    }

    @Override
    public void onClose(Consumer<UnitOfWork<T>> handler) {
        addHandler(Phase.CLOSED, handler);
    }

    protected abstract void setExecutionResult(ExecutionResult executionResult);
    protected abstract void addHandler(Phase phase, Consumer<UnitOfWork<T>> handler);
    protected abstract void notifyHandlers(Phase phase);
    protected abstract void setRollbackCause(Throwable throwable);
}
