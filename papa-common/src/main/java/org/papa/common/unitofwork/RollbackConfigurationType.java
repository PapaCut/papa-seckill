package org.papa.common.unitofwork;

/**
 * Created by PaperCut on 2017/11/16.
 */
public enum RollbackConfigurationType implements RollbackConfiguration{
    NEVER{
        @Override
        public boolean rollbackOn(Throwable cause) {
            return false;
        }
    },

    ANY_THROWABLE
    {
        @Override
        public boolean rollbackOn(Throwable cause) {
            return true;
        }
    },
    UNCHECKED_EXCEPTIONS
    {
        @Override
        public boolean rollbackOn(Throwable cause) {
            return !(cause instanceof Exception) || (cause instanceof RuntimeException);
        }
    },
    RUNTIME_EXCEPTIONS
    {
        @Override
        public boolean rollbackOn(Throwable cause) {
            return cause instanceof RuntimeException;
        }
    }

}
