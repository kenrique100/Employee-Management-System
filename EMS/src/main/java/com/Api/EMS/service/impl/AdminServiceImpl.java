package com.Api.EMS.service.impl;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.security.JwtTokenProvider;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.utils.GUIDGenerator;
import com.Api.EMS.validation.UserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminServiceImpl(UserRepository userRepository, UserValidation userValidation, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse signup(AuthRequest authRequest) {
        if (authRequest.getRoles() == null || !authRequest.getRoles().contains("ADMIN")) {
            throw new IllegalArgumentException("Only admins are allowed to sign up");
        }

        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new IllegalArgumentException("Username is taken");
        }

        User user = User.builder()
                .companyName(authRequest.getCompanyName())
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .roles(List.of("ADMIN"))
                .build();

        User savedUser = userRepository.save(user);
        return new AuthResponse(jwtTokenProvider.generateToken(savedUser));
    }

    @Override
    public User createUser(UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        User user = populateUserFields(new User(), userDTO);
        user.setGuid(GUIDGenerator.generateGUID(8));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> updateUser(Long id, UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        return userRepository.findById(id).map(user -> {
            populateUserFields(user, userDTO);
            return userRepository.save(user);
        });
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

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
