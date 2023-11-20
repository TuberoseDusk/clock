package com.tuberose.clock.user.mapper;

import com.tuberose.clock.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int count();

    int insert(User user);

    User selectByUserId(Long userId);

    User selectByUsername(String name);
}
