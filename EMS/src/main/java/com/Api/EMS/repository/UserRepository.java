package com.Api.EMS.repository;

import com.Api.EMS.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> { // Use String for MongoDB ObjectId

    // Method to find a user by username
    Mono<User> findByUsername(String username);

    // Check if a user exists by username
    default Mono<Boolean> existsByUsername(String username) {
        return findByUsername(username)
                .map(user -> true)
                .defaultIfEmpty(false);
    }
}
