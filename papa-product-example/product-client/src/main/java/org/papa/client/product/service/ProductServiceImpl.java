package org.papa.client.product.service;

import org.papa.product.ProductDto;
import org.papa.seckill.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by PaperCut on 2018/3/1.
 */
@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public void add(int userId, int itemId) {
        RequestDto<ProductDto> requestDto = new RequestDto<>();
        ProductDto productDto = new ProductDto(userId, itemId);
        requestDto.setPayload(productDto);
        jmsTemplate.convertAndSend(requestDto);
    }
}
