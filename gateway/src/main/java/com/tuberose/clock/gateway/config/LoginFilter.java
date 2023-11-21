package com.tuberose.clock.gateway.config;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.gateway.util.JWTDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class LoginFilter implements Ordered, GlobalFilter {

    private static Set<String> whiteList = new HashSet<>();
    static {
        whiteList.add("/user/welcome");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        for (String whitePath : whiteList) {
            if (path.startsWith(whitePath)) {
                log.info("visit the whitelist page: {}", whitePath);
                return chain.filter(exchange);
            }
        }

        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (BeanUtil.isEmpty(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (JWTDecoder.validate(token)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
