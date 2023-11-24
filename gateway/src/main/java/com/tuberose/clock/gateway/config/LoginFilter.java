package com.tuberose.clock.gateway.config;

import com.tuberose.clock.gateway.util.JWTValidator;
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
        whiteList.add("/user/test/hello");
        whiteList.add("/business");
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
        if (token == null || token.trim().isEmpty()) {
            log.error("token is empty.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (JWTValidator.validate(token)) {
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
