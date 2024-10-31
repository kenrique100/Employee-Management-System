package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.DirectorService;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/director")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return ResponseUtil.handleMonoResponse(directorService.createUser(userDTO));
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody UserDTO<String> userDTO) {
        return ResponseUtil.handleMonoResponse(directorService.updateUser(id, userDTO));
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return ResponseUtil.handleVoidResponse(directorService.deleteUser(id));
    }

    @GetMapping("/users")
    public Flux<ResponseEntity<User>> getAllUsers() {
        return directorService.getAllUsers()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
        return ResponseUtil.handleMonoResponse(directorService.findUserById(id));
    }
}
