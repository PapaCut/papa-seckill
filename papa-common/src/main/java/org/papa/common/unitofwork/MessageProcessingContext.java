package org.papa.common.unitofwork;

import org.papa.common.Message;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.function.Consumer;

import org.papa.common.unitofwork.UnitOfWork.Phase;

/**
 * Created by PaperCut on 2017/11/16.
 */
public class MessageProcessingContext<T extends Message> {
    private static final Deque EMPTY = new LinkedList();

    private final EnumMap<Phase, Deque<Consumer<UnitOfWork<T>>>> handlers = new EnumMap<Phase, Deque<Consumer<UnitOfWork<T>>>>(Phase.class);

    private T message;
    private ExecutionResult executionResult;

    public void addHandler(Phase phase, Consumer<UnitOfWork<T>> consumer)
    {
        Deque<Consumer<UnitOfWork<T>>> consumers = handlers.computeIfAbsent(phase, p -> new ArrayDeque<Consumer<UnitOfWork<T>>>());
        if(phase.isReverseCallbackOrder())
        {
            consumers.addFirst(consumer);
        }
        else
        {
            consumers.add(consumer);
        }
    }

    public void notifyHandlers(UnitOfWork<T> unitOfWork, Phase phase)
    {
        Deque<Consumer<UnitOfWork<T>>> list = handlers.getOrDefault(phase, EMPTY);
        while(!list.isEmpty())
        {
            list.poll().accept(unitOfWork);
        }
    }

    public void setExecutionResult(ExecutionResult executionResult) {
        this.executionResult = executionResult;
    }

    public MessageProcessingContext(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }

    public ExecutionResult getExecutionResult() {
        return executionResult;
    }

    public void reset(T message)
    {
        this.message = message;
        handlers.clear();
        executionResult = null;
    }
}
