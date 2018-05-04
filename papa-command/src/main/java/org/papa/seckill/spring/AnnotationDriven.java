package org.papa.seckill.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by PaperCut on 2018/4/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AnnotationRegistrar.class)
public @interface AnnotationDriven {

}
