package com.tuberose.clock.gateway.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JWTValidator {
    private static final String key = "clock";

    public static boolean validate(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        boolean validate = jwt.validate(0);
        log.info("JWT token is {}", validate);
        return validate;
    }
}
