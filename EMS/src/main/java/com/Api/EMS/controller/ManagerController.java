
package com.Api.EMS.controller;

import com.Api.EMS.model.User;
import com.Api.EMS.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController extends UserControllerBase {

    public ManagerController(UserService<User> userService) {
        super(userService);
    }
}
