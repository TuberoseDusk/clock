package com.tuberose.clock.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.user.entity.User;
import com.tuberose.clock.user.mapper.UserMapper;
import com.tuberose.clock.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public int getUserNumber() {
        return userMapper.count();
    }

    @Override
    public User register(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (BeanUtil.isNotEmpty(user)) {
            log.info("the user name is duplicate.");
            return null;
        }
        Long userId = 1L;
        user = new User(userId, username,
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        if (userMapper.insert(user) != 1) {
            log.info("failed to insert user.");
        }
        return user;
    }
}
