package com.Api.EMS.utils;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.Role;
import com.Api.EMS.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {

    private UserUtil() {
    }

    public static User populateUserFields(User user, UserDTO<String> userDTO) {
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setNationalIdNumber(userDTO.getNationalIdNumber());
        user.setDateOfEmployment(userDTO.getDateOfEmployment());
        user.setSpecialty(userDTO.getSpecialty());


        List<Role> roles = userDTO.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toList());
        user.setRoles(roles);

        user.setCompanyName(userDTO.getCompanyName());
        return user;
    }
}
