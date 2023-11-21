package com.tuberose.clock.common.context;

public class UserHolder {
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userIdTL.set(userId);
    }

    public static Long getUserId() {
        return userIdTL.get();
    }
}
