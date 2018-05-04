package org.papa.seckill.command.annotation;


import org.papa.seckill.command.Command;
import org.papa.seckill.command.CommandChannel;
import org.papa.seckill.command.normal.SimpleCommandChannel;

import java.lang.annotation.*;

/**
 * Created by PaperCut on 2018/2/26.
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {
    Class<? extends Command> command();
    Class<? extends CommandChannel> channel() default SimpleCommandChannel.class;
}
