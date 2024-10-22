package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.exception.ResourceNotFoundException;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.DirectorService;
import com.Api.EMS.utils.GUIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> createUser(UserDTO userDTO) {
        User user = User.builder()
                .guid(GUIDGenerator.generateGUID(8))
                .name(userDTO.getName())
                .age(userDTO.getAge())
                .gender(userDTO.getGender())
                .specialty(userDTO.getSpecialty())  // Ensure specialty is defined
                .dateOfEmployment(userDTO.getDateOfEmployment())
                .roles(Arrays.asList(userDTO.getRoles()))  // Convert roles array to list
                .build();
        return Mono.just(userRepository.save(user));
    }

    @Override
    public Mono<User> updateUser(Long id, UserDTO userDTO) {
        return Mono.just(userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)))
                .map(user -> {
                    user.setName(userDTO.getName());
                    user.setAge(userDTO.getAge());
                    user.setGender(userDTO.getGender());
                    user.setSpecialty(userDTO.getSpecialty());  // Ensure this setter exists
                    user.setDateOfEmployment(userDTO.getDateOfEmployment());
                    user.setRoles(Arrays.asList(userDTO.getRoles()));  // Convert roles array to list
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return Mono.justOrEmpty(userRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with id: " + id)))
                .flatMap(user -> {
                    userRepository.delete(user);
                    return Mono.empty();  // Return an empty Mono<Void>
                });
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return Mono.just(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }
}
