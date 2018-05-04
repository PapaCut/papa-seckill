package org.papa.server.product.event;

import org.papa.product.ProductDto;
import org.papa.seckill.RequestDto;
import org.papa.seckill.RequestHandler;
import org.papa.seckill.ResponseDto;
import org.papa.seckill.event.disruptor.RequestEvent;
import org.papa.server.product.command.ProductInsertCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PaperCut on 2018/3/1.
 */
public class ProductHandler implements RequestHandler<ProductDto> {
    private static final Logger logger = LoggerFactory.getLogger(ProductHandler.class);

    @Override
    public RequestEvent<ProductDto> onEvent(RequestEvent<ProductDto> event) {

        try {
            RequestDto<ProductDto> requestDto = event.getRequestDto();
            ProductDto productDto = requestDto.getPayload();
            logger.info("Product itemId: {}", productDto.getItemId());
            logger.info("Product userId: {}", productDto.getUserId());

            // 放入新增命令
            event.getCommandCollector().add(new ProductInsertCommand(requestDto.getId()));

            // 产生执行结果
            ResponseDto response = new ResponseDto(requestDto.getId());
            response.setSuccess(true);

            event.setResponseDto(response);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(event.getRequestDto().getId());
            responseDto.setSuccess(false);
            responseDto.setErrMsg(e.getMessage());
            event.setResponseDto(responseDto);

            logger.error("Failed to execute insert product.");
        }

        return event;
    }
}
