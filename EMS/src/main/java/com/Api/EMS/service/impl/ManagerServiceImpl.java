package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.ManagerService;
import com.Api.EMS.utils.GUIDGenerator;
import com.Api.EMS.utils.UserUtil;
import com.Api.EMS.validation.UserValidation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final UserRepository userRepository;
    private final UserValidation userValidation;

    public ManagerServiceImpl(UserRepository userRepository, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
    }

    @Override
    public Mono<User> createUser(UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        User user = UserUtil.populateUserFields(new User(), userDTO);
        user.setGuid(GUIDGenerator.generateGUID(8));
        return saveUser(user);
    }

    @Override
    public Mono<User> updateUser(String id, UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        return findUserById(id)
                .flatMap(user -> {
                    User updatedUser = UserUtil.populateUserFields(user, userDTO);
                    return saveUser(updatedUser);
                });
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return findUserById(id)
                .flatMap(user -> userRepository.delete(user)
                        .subscribeOn(Schedulers.boundedElastic())
                        .then());
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<User> findUserById(String id) {
        return userRepository.findById(id)
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    private Mono<User> saveUser(User user) {
        return userRepository.save(user)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
