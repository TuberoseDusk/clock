package com.tuberose.clock.user.service;

import com.tuberose.clock.user.entity.User;

public interface UserService {
    int getUserNumber();
    User register(String username, String password);
}
