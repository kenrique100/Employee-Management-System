package com.Api.EMS.repository;

import com.Api.EMS.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    default Mono<Boolean> existsByUsername(String username) {
        return findByUsername(username)
                .map(user -> true)
                .defaultIfEmpty(false);
    }

    Mono<User> findByUsername(String username);
}