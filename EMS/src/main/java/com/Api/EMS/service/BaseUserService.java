package com.Api.EMS.service;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public abstract class BaseUserService<T extends User> implements UserService<T> {

    protected final UserRepository userRepository;

    @Override
    public Mono<T> createUser(UserDTO<String> userDTO) {
        T user = createUserEntity(userDTO);
        return userRepository.save(user).cast(getEntityClass());
    }

    protected abstract T createUserEntity(UserDTO<String> userDTO);

    @Override
    public Mono<T> updateUser(String id, UserDTO<String> userDTO) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.updateFromDTO(userDTO);
                    return userRepository.save(existingUser).cast(getEntityClass());
                });
    }

    @Override
    public Mono<Boolean> deleteUser(String id) {
        return userRepository.findById(id)
                .flatMap(user -> userRepository.delete(user).thenReturn(true))
                .defaultIfEmpty(false);
    }

    @Override
    public Flux<T> getAllUsers() {
        return userRepository.findAll().cast(getEntityClass());
    }

    @Override
    public Mono<T> getUserById(String id) {
        return userRepository.findById(id).cast(getEntityClass());
    }


    protected abstract Class<T> getEntityClass();
}
