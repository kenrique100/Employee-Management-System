package com.Api.EMS.repository;

import com.Api.EMS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    default boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}
