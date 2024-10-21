package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.exception.ResourceNotFoundException;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.utils.GUIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> createUser(UserDTO userDTO) {
        User user = User.builder()
                .guid(GUIDGenerator.generateGUID(8))  // Ensure GUID is generated
                .name(userDTO.getName())
                .age(userDTO.getAge())
                .gender(userDTO.getGender())
                .specialty(userDTO.getSpecialty())
                .dateOfEmployment(userDTO.getDateOfEmployment())
                .roles(userDTO.getRoles())
                .build();

        return Mono.just(userRepository.save(user));
    }

    @Override
    public Mono<User> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDTO.getName());
                    user.setAge(userDTO.getAge());
                    user.setGender(userDTO.getGender());
                    user.setSpecialty(userDTO.getSpecialty());
                    user.setDateOfEmployment(userDTO.getDateOfEmployment());
                    user.setRoles(userDTO.getRoles());
                    return userRepository.save(user);
                }).switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with id: " + id)));
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return Mono.<Void>empty();
                }).switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with id: " + id)));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return userRepository.findById(id)
                .map(Mono::just)
                .orElse(Mono.error(new ResourceNotFoundException("User not found with id: " + id)));
    }


}
