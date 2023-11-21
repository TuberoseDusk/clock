package com.tuberose.clock.user.service;

import com.tuberose.clock.user.entity.User;
import com.tuberose.clock.user.response.UserRes;

public interface UserService {
    int getUserNumber();
    User register(String username, String password);

    UserRes login(String username, String password);
}
