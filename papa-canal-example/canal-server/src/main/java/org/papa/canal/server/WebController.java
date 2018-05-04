package org.papa.canal.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by PaperCut on 2018/3/10.
 */
@RestController
@RequestMapping("/")
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @RequestMapping("/")
    public String home() {
        logger.info("111111126j");
        return "success";
    }
}
