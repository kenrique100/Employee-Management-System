// EmployeeController.java
package com.Api.EMS.controller;

import com.Api.EMS.model.User;
import com.Api.EMS.service.UserService;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService<User> userService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .flatMap(ResponseUtil::createSuccessResponse)
                .switchIfEmpty(ResponseUtil.createNotFoundResponse(User.class));
    }

    @GetMapping("/error")
    public Mono<ResponseEntity<String>> generateErrorResponse() {
        return ResponseUtil.createErrorResponse("Something went wrong", HttpStatus.BAD_REQUEST);
    }
}
