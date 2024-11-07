package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> createUser(UserDTO userDTO) {
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(userDTO.getRoles())
                .name(userDTO.getName())
                .age(userDTO.getAge())
                .gender(userDTO.getGender())
                .nationalIdNumber(userDTO.getNationalIdNumber())
                .dateOfEmployment(userDTO.getDateOfEmployment())
                .specialty(userDTO.getSpecialty())
                .companyName(userDTO.getCompanyName())
                .build();
        return userRepository.save(user);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> updateUser(String id, UserDTO userDTO) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(userDTO.getUsername());
                    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    }
                    existingUser.setRoles(userDTO.getRoles());
                    existingUser.setName(userDTO.getName());
                    existingUser.setAge(userDTO.getAge());
                    existingUser.setGender(userDTO.getGender());
                    existingUser.setNationalIdNumber(userDTO.getNationalIdNumber());
                    existingUser.setDateOfEmployment(userDTO.getDateOfEmployment());
                    existingUser.setSpecialty(userDTO.getSpecialty());
                    existingUser.setCompanyName(userDTO.getCompanyName());
                    return userRepository.save(existingUser);
                });
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id);
    }
}
