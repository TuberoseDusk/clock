package com.tuberose.clock.user.service.impl;

import com.tuberose.clock.user.mapper.UserMapper;
import com.tuberose.clock.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public int getUserNumber() {
        return userMapper.count();
    }
}
