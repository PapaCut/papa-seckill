package org.papa.seckill.event;

import java.lang.annotation.*;

/**
 * Created by PaperCut on 2018/3/1.
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sender {

}
