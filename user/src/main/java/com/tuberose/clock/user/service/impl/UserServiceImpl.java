package com.tuberose.clock.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.util.JWTEncoder;
import com.tuberose.clock.common.util.Snowflake;
import com.tuberose.clock.user.entity.User;
import com.tuberose.clock.user.mapper.UserMapper;
import com.tuberose.clock.user.response.UserRes;
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
            log.error("the user name is duplicate.");
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }
        Long userId = Snowflake.nextId();
        user = new User(userId, username,
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        return userMapper.insert(user) == 1 ? user : null;
    }

    @Override
    public UserRes login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (BeanUtil.isEmpty(user)) {
            log.error("the user name does not exist.");
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(md5Password)) {
            log.error("wrong password.");
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }
        UserRes userRes = new UserRes();
        BeanUtil.copyProperties(user, userRes);

        String token = JWTEncoder.encode(user.getUserId(), user.getUsername());
        userRes.setToken(token);
        return userRes;
    }
}
