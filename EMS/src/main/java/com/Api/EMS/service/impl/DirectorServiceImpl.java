package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.DirectorService;
import com.Api.EMS.utils.UserUtil;
import com.Api.EMS.validation.UserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder;

    public DirectorServiceImpl(UserRepository userRepository, UserValidation userValidation, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> createUser(UserDTO<String> userDTO) {
        return userRepository.existsByUsernameAndCompanyName(userDTO.getUsername(), userDTO.getCompanyName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("User with this username and company name already exists"));
                    }
                    User user = new User();
                    UserUtil.populateUserFields(user, userDTO);
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<User> updateUser(String id, UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        return userRepository.findById(id)
                .flatMap(user -> {
                    UserUtil.populateUserFields(user, userDTO);
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return userRepository.findById(id)
                .flatMap(userRepository::delete);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findUserById(String id) {
        return userRepository.findById(id);
    }
}
