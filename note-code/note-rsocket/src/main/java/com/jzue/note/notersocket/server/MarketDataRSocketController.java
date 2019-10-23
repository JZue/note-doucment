package com.jzue.note.notersocket.server;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author jzue
 * @date 2019/9/25 上午10:05
 **/
@Controller
public class MarketDataRSocketController {


    @MessageMapping("/serverCurrentMarketData")
    public String currentMarketData(String message){
        return message;
    }
}
