package com.example.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class RequestIdGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String requestId = exchange.getRequest()
                    .getHeaders()
                    .getFirst(REQUEST_ID_HEADER);
            
            if (requestId == null) {
                requestId = UUID.randomUUID().toString();
            }
            
            log.info("Incoming request: {} - RequestID: {}", 
                    exchange.getRequest().getPath(), requestId);
            
            return chain.filter(exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header(REQUEST_ID_HEADER, requestId)
                            .build())
                    .response(exchange.getResponse().mutate()
                            .header(REQUEST_ID_HEADER, requestId)
                            .build())
                    .build());
        };
    }
}
