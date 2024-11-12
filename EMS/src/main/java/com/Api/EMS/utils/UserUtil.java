package com.Api.EMS.utils;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.Role;
import com.Api.EMS.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {

    public static User populateUserFields(User user, UserDTO<String> userDTO, PasswordEncoder passwordEncoder) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(userDTO.getRoles());
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setTelephoneNumber(userDTO.getTelephoneNumber());
        user.setGender(userDTO.getGender());
        user.setNationalIdNumber(userDTO.getNationalIdNumber());
        user.setDateOfEmployment(userDTO.getDateOfEmployment());
        user.setSpecialty(userDTO.getSpecialty());
        return user;
    }

    public static User populateUserFields(User user, AuthRequest authRequest, PasswordEncoder passwordEncoder) {
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        List<Role> roleObjects = authRequest.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toList());
        user.setRoles(roleObjects);
        return user;
    }
}
