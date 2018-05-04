package org.papa.common.unitofwork;

/**
 * Created by PaperCut on 2017/11/16.
 */
public interface RollbackConfiguration {
    boolean rollbackOn(Throwable cause);

}
