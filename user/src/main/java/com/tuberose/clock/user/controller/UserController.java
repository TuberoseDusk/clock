package com.tuberose.clock.user.controller;

import com.tuberose.clock.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/userNumber")
    public int userNumber() {
        return userService.getUserNumber();
    }

}
