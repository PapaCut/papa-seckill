package org.papa.seckill;

import java.lang.annotation.*;

/**
 * Created by PaperCut on 2018/2/27.
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
    Class<? extends RequestProcessor> type();
}
