package com.Api.EMS.controller;

import com.Api.EMS.model.User;
import com.Api.EMS.service.EmployeeService;
import com.Api.EMS.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Constructor injection
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public Mono<Mono<ResponseEntity<User>>> getUserById(@PathVariable String id) {
        return employeeService.getUserById(id)
                .map(ResponseUtil::createSuccessResponse)
                .defaultIfEmpty(ResponseUtil.createNotFoundResponse(User.class));  // No blocking call
    }
}



