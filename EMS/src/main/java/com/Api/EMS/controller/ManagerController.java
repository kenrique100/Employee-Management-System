package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.ManagerService;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return ResponseUtil.handleMonoResponse(managerService.createUser(userDTO));
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody UserDTO<String> userDTO) {
        return ResponseUtil.handleMonoResponse(managerService.updateUser(id, userDTO));
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return ResponseUtil.handleVoidResponse(managerService.deleteUser(id));
    }

    @GetMapping("/users")
    public Flux<ResponseEntity<User>> getAllUsers() {
        return managerService.getAllUsers()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
        return ResponseUtil.handleMonoResponse(managerService.findUserById(id));
    }
}
