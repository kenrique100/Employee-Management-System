package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/users")
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody UserDTO userDTO) {
        return managerService.createUser(userDTO)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/users/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return managerService.updateUser(id, userDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/users/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return managerService.deleteUser(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return managerService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable Long id) {
        return managerService.findUserById(id)
                .map(ResponseEntity::ok);
    }
}
