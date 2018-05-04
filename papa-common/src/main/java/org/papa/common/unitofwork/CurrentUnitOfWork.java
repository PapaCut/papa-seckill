package org.papa.common.unitofwork;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Created by PaperCut on 2017/11/16.
 */
public abstract class CurrentUnitOfWork {
    private static final ThreadLocal<Deque<UnitOfWork<?>>> CURRENT = new ThreadLocal<>();

    public static boolean isStarted()
    {
        return CURRENT.get() != null && !CURRENT.get().isEmpty();
    }

    public static boolean ifStarted(Consumer<UnitOfWork<?>> consumer)
    {
        if(isStarted())
        {
            consumer.accept(get());
            return true;
        }
        return false;
    }

    public static boolean isEmpty()
    {
        Deque<UnitOfWork<?>> unitOfWork = CURRENT.get();
        return unitOfWork == null || unitOfWork.isEmpty();
    }

    public static UnitOfWork get()
    {
        if(isEmpty())
        {
            throw new IllegalStateException("Not found UnitOfWork in current thread");
        }
        return CURRENT.get().peek();
    }

    public static void commit()
    {
        get().commit();
    }

    public static void clear(UnitOfWork<?> unitOfWork)
    {
        if(!isStarted())
        {
            throw new IllegalStateException();
        }

        if(CURRENT.get().peek() == unitOfWork)
        {
            CURRENT.get().pop();
            if(CURRENT.get().isEmpty())
            {
                CURRENT.remove();
            }
        }
        else
        {
            throw new IllegalStateException();
        }
    }

    public static void set(UnitOfWork<?> unitOfWork)
    {
        if(CURRENT.get() == null)
        {
            CURRENT.set(new LinkedList<>());
        }
        CURRENT.get().push(unitOfWork);
    }
}
