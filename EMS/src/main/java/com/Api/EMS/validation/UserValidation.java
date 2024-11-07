package com.Api.EMS.validation;

import com.Api.EMS.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final List<String> VALID_GENDERS = List.of("Male", "Female", "Other");

    public void validateUser(UserDTO userDTO) {
        validateName(userDTO.getName());
        validateAge(userDTO.getAge());
        validateGender(userDTO.getGender());
        validateSpecialty(userDTO.getSpecialty());
        validateRoles(userDTO.getRoles());
    }

    private void validateName(String name) {
        if (isBlank(name) || !NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }
    }

    private void validateAge(int age) {
        if (age < 18 || age > 65) {
            throw new IllegalArgumentException("Age must be between 18 and 65.");
        }
    }

    private void validateGender(String gender) {
        if (isBlank(gender) || !VALID_GENDERS.contains(gender)) {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }

    private void validateSpecialty(String specialty) {
        if (isBlank(specialty)) {
            throw new IllegalArgumentException("Specialty cannot be empty.");
        }
    }

    private void validateRoles(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be empty.");
        }
        roles.forEach(role -> {
            if (isBlank(role)) throw new IllegalArgumentException("Role cannot be empty.");
        });
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
