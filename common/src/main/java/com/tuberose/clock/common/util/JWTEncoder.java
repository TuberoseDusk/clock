package com.tuberose.clock.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTEncoder {
    private static final String key = "clock";

    public static String encode(Long userId) {
        DateTime now = DateTime.now();
        DateTime expire = now.offsetNew(DateField.MONTH, 1);
        Map<String, Object> payload = new HashMap<>();
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, expire);
        payload.put(JWTPayload.NOT_BEFORE, now);

        payload.put("userId", userId);

        String token = JWTUtil.createToken(payload, key.getBytes());
        log.info("create token: {}", token);
        return token;
    }
}
