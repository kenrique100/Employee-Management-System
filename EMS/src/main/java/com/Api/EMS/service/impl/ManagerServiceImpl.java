package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.ManagerService;
import com.Api.EMS.utils.GUIDGenerator;
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
        User user = populateUserFields(new User(), userDTO);
        user.setGuid(GUIDGenerator.generateGUID(8));
        return saveUser(user);
    }

    @Override
    public Mono<User> updateUser(Long id, UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        return findUserById(id)
                .flatMap(user -> {
                    populateUserFields(user, userDTO);
                    return saveUser(user);
                });
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return findUserById(id)
                .flatMap(user -> userRepository.delete(user) // Delete method should return Mono<Void>
                        .subscribeOn(Schedulers.boundedElastic())
                        .then());
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll() // Directly return the Flux<User> from the repository
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return userRepository.findById(id) // This returns Mono<User>
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    private Mono<User> saveUser(User user) {
        return userRepository.save(user) // Assuming save returns Mono<User>
                .subscribeOn(Schedulers.boundedElastic());
    }

    // Reusable method to populate user fields
    private User populateUserFields(User user, UserDTO<String> userDTO) {
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setNationalIdNumber(userDTO.getNationalIdNumber());
        user.setDateOfEmployment(userDTO.getDateOfEmployment());
        user.setRoles(userDTO.getRoles());
        return user;
    }
}
