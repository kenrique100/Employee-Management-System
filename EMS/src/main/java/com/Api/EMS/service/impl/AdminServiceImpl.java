package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.utils.GUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<User> createUser(UserDTO userDTO) {
        User user = new User();
        user.setGuid(GUIDGenerator.generateGUID(8));
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setNationalIdNumber(userDTO.getNationalIdNumber());
        user.setDateOfEmployment(userDTO.getDateOfEmployment());
        user.setRoles(userDTO.getRoles());  // No need to wrap in List.of()
        return Mono.just(userRepository.save(user));
    }

    @Override
    public Mono<User> updateUser(Long id, UserDTO userDTO) {
        return Mono.justOrEmpty(userRepository.findById(id)).flatMap(user -> {
            user.setName(userDTO.getName());
            user.setAge(userDTO.getAge());
            user.setGender(userDTO.getGender());
            user.setNationalIdNumber(userDTO.getNationalIdNumber());
            user.setDateOfEmployment(userDTO.getDateOfEmployment());
            user.setRoles(userDTO.getRoles());  // No need to wrap in List.of()
            return Mono.just(userRepository.save(user));
        });
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return Mono.justOrEmpty(userRepository.findById(id)).flatMap(user -> {
            userRepository.delete(user);
            return Mono.empty();
        });
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return Mono.justOrEmpty(userRepository.findById(id));
    }
}
