package com.Api.EMS.repository;

import com.Api.EMS.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> { // Use String for MongoDB ObjectId

    // Method to find a user by username and company name
    Mono<User> findByUsernameAndCompanyName(String username, String companyName);

    // Check if a user exists by username and company name
    default Mono<Boolean> existsByUsernameAndCompanyName(String username, String companyName) {
        return findByUsernameAndCompanyName(username, companyName)
                .map(user -> true)
                .defaultIfEmpty(false);
    }
}
