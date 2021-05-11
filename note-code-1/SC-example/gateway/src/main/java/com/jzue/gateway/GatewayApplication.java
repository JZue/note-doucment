package com.jzue.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator mRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.host("**.changeuri.org").and().header("X-Next-Url")
                        .uri("http://blueskykong.com"))
                .route(r -> r.host("**.changeuri.org").and().query("url")
                        .uri("http://blueskykong.com"))
                .build();

    }

}
