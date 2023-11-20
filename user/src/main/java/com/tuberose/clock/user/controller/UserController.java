package com.tuberose.clock.user.controller;

import com.tuberose.clock.common.response.BaseRes;
import com.tuberose.clock.user.entity.User;
import com.tuberose.clock.user.request.RegisterReq;
import com.tuberose.clock.user.response.UserRes;
import com.tuberose.clock.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/userNumber")
    public BaseRes<Integer> userNumber() {
        return BaseRes.success(userService.getUserNumber());
    }

    @PostMapping("/register")
    public BaseRes<UserRes> register(@Valid @RequestBody RegisterReq registerReq) {
        User user = userService.register(registerReq.getUsername(), registerReq.getPassword());
        UserRes userRes = new UserRes(user.getUserId(), user.getPassword());
        return BaseRes.success(userRes);
    }

}
