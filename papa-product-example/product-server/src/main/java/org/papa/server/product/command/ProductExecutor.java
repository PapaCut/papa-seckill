package org.papa.server.product.command;

import org.papa.seckill.command.CommandBuffer;
import org.papa.seckill.command.CommandExecutor;
import org.papa.seckill.command.annotation.CommandHandler;
import org.papa.seckill.command.disruptor.DisruptorCommandChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/2/19.
 */
@CommandHandler(command = ProductInsertCommand.class, channel = DisruptorCommandChannel.class)
@Component
public class ProductExecutor implements CommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ProductExecutor.class);

    @Override
    public void execute(CommandBuffer buffer) {
        for(Object obj : buffer.get()) {
            logger.info("Invoke product executor...");
        }
    }
}
