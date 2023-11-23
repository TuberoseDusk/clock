package com.tuberose.clock.common.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

public class JWTDecoder {
    private static final String key = "clock";

    public static Long decode(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
        return ((NumberWithFormat)payloads.get("userId")).longValue();
    }
}
