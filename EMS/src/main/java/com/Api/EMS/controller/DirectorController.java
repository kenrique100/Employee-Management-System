package com.Api.EMS.controller;

import com.Api.EMS.model.User;
import com.Api.EMS.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/director")
public class DirectorController extends UserControllerBase {

    public DirectorController(UserService<User> userService) {
        super(userService);
    }
}