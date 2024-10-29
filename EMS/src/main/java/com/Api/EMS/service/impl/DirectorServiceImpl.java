package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.DirectorService;
import com.Api.EMS.utils.GUIDGenerator;
import com.Api.EMS.validation.UserValidation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final UserRepository userRepository;
    private final UserValidation userValidation;

    public DirectorServiceImpl(UserRepository userRepository, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
    }

    @Override
    public Mono<User> createUser(UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        User user = populateUserFields(new User(), userDTO);
        user.setGuid(GUIDGenerator.generateGUID(8));
        return userRepository.save(user);
    }

    @Override
    public Mono<User> updateUser(String id, UserDTO<String> userDTO) { // ID is String
        userValidation.validateUser(userDTO);
        return userRepository.findById(id)
                .flatMap(user -> {
                    populateUserFields(user, userDTO);
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<Void> deleteUser(String id) { // ID is String
        return userRepository.findById(id)
                .flatMap(user -> userRepository.delete(user).then());
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findUserById(String id) { // ID is String
        return userRepository.findById(id);
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
