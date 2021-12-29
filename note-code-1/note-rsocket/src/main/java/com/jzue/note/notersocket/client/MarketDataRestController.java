package com.jzue.note.notersocket.client;

import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jzue
 * @date 2019/9/25 上午10:08
 **/
@RestController
public class MarketDataRestController {

    @Resource
    private  RSocketRequester rSocketRequester;




    /**
     * description:
     * @param message 你好
     * @return : org.reactivestreams.Publisher<java.lang.String>
     * @author jzue
     * @date 2019/9/25
     */
    @GetMapping(value = "/client/current/{message}")

    public Publisher<String> current(@PathVariable String message){
        return rSocketRequester
                .route("/serverCurrentMarketData")
                .data(message)
                .retrieveMono(String.class);
    }

}
