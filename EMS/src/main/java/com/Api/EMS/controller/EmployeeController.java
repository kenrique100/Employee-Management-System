package com.Api.EMS.controller;

import com.Api.EMS.model.User;
import com.Api.EMS.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/profile/{id}")
    public Mono<ResponseEntity<User>> getProfile(@PathVariable Long id) {
        return employeeService.getUserById(id)
                .map(ResponseEntity::ok);
    }
}
