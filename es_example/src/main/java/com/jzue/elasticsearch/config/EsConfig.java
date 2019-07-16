package com.jzue.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: junzexue
 * @Date: 2019/7/15 上午8:51
 * @Description:
 **/
@Configuration
public class EsConfig {

    @Bean
    public RestHighLevelClient client(){
        RestHighLevelClient client =new RestHighLevelClient(
                RestClient.builder(new HttpHost("127.0.0.1",9200,"http")));
        return client;
    }
}
