package com.Api.EMS.repository;

import com.Api.EMS.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByUsernameAndCompanyName(String username, String companyName);

    default Mono<Boolean> existsByUsernameAndCompanyName(String username, String companyName) {
        return findByUsernameAndCompanyName(username, companyName)
                .map(user -> true)
                .defaultIfEmpty(false);
    }
}