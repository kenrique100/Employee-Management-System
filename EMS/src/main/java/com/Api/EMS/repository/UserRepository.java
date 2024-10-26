package com.Api.EMS.repository;

import com.Api.EMS.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    // This method checks if a user with the given username exists
    default Mono<Boolean> existsByUsername(String username) {
        return findByUsername(username)
                .map(user -> true) // If user is found, return true
                .defaultIfEmpty(false); // If not found, return false
    }

    // Retrieve a user by username
    Mono<User> findByUsername(String username);
}
