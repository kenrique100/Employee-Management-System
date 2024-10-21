package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/director")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping("/users")
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody UserDTO userDTO) {
        return directorService.createUser(userDTO)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/users/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return directorService.updateUser(id, userDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/users/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return directorService.deleteUser(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return directorService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable Long id) {
        return directorService.findUserById(id)
                .map(ResponseEntity::ok);
    }
}
