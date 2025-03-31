package com.four.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @BelongsProject: four-payment-demo
 * @BelongsPackage: com.four.payment.request
 * @Author: george
 * @CreateTime: 2024-12-30  16:01
 * @Description:
 * @Version: 1.0.0
 */
@Slf4j
@RestController
public class TestController {

    @RequestMapping(value = "/https/send", method = RequestMethod.POST)
    public Object httpsSend(@RequestBody String body)throws Exception{
        log.info(" TestController   body:{}",body);
        System.err.println(body);
        return true;
    }

}
