package com.jzue.note.notersocket.config;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;


/**
 * @author jzue
 * @date 2019/9/25 上午9:14
 **/
@Configuration
public class ClientConfiguration {

    @Bean
    public RSocket rSocket(){
        return RSocketFactory
                .connect()
                .mimeType(MimeTypeUtils.APPLICATION_JSON_VALUE,MimeTypeUtils.APPLICATION_JSON_VALUE)
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .transport(TcpClientTransport.create(7000))
                .start()
                .block();
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies){
        return RSocketRequester.wrap(rSocket(),MimeTypeUtils.APPLICATION_JSON,MimeTypeUtils.APPLICATION_JSON,rSocketStrategies);
    }
}
