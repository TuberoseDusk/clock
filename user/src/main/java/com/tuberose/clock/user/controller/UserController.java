package com.tuberose.clock.user.controller;

import com.tuberose.clock.common.response.BaseRes;
import com.tuberose.clock.user.entity.User;
import com.tuberose.clock.user.request.UserReq;
import com.tuberose.clock.user.response.UserRes;
import com.tuberose.clock.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/welcome")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/userNumber")
    public BaseRes<Integer> userNumber() {
        return BaseRes.success(userService.getUserNumber());
    }

    @PostMapping("/register")
    public BaseRes<UserRes> register(@Valid @RequestBody UserReq registerReq) {
        User user = userService.register(registerReq.getUsername(), registerReq.getPassword());
        UserRes userRes = new UserRes(user.getUserId(), user.getPassword(), null);
        return BaseRes.success(userRes);
    }

    @PostMapping("/login")
    public BaseRes<UserRes> login(@Valid @RequestBody UserReq loginReq) {
        UserRes userRes = userService.login(loginReq.getUsername(), loginReq.getPassword());
        return BaseRes.success(userRes);
    }

}
