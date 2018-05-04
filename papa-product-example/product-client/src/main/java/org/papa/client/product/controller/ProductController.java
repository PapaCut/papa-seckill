package org.papa.client.product.controller;

import org.papa.client.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by PaperCut on 2018/3/1.
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @RequestMapping("add")
    public String add() {
        productService.add(1, 12);
        return "success";
    }
}
