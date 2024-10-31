package com.Api.EMS.utils;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;

public class UserUtil {

    private UserUtil() {
    }

    public static User populateUserFields(User user, UserDTO<String> userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setRoles(userDTO.getRoles());
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setSpecialty(userDTO.getSpecialty());
        user.setNationalIdNumber(userDTO.getNationalIdNumber());
        user.setDateOfEmployment(userDTO.getDateOfEmployment());
        user.setCompanyName(userDTO.getCompanyName());
        return user;
    }
}
