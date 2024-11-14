package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.model.User;
import com.Api.EMS.service.UserService;
import com.Api.EMS.utils.AuthValidationUtil;
import com.Api.EMS.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin")
public class AdminController extends UserControllerBase {

    public AdminController(UserService<User> userService) {
        super(userService);
    }
    @PostMapping("/signup")
    public Mono<ResponseEntity<AuthResponse>> signupAdmin(@RequestBody AuthRequest authRequest) {
        return AuthValidationUtil.validateAdminRole(authRequest)
                .flatMap(validRequest ->
                        userService.signup(validRequest)
                                .flatMap(ResponseUtil::handleMonoResponse)
                )
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse("Invalid admin role", null, null)));
    }

}
