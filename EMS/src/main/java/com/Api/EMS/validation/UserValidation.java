package com.Api.EMS.validation;

import com.Api.EMS.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    public void validateUser(UserDTO<String> userDTO) {
        validateName(userDTO.getName());
        validateAge(userDTO.getAge());
        validateGender(userDTO.getGender());
        validateSpecialty(userDTO.getSpecialty());
        validateRoles(userDTO.getRoles());
    }

    private void validateName(String name) {
        boolean isInvalidName = (name == null || name.trim().isEmpty() || !NAME_PATTERN.matcher(name).matches());
        throwExceptionIfTrue(isInvalidName, "Invalid name: " + name);
    }

    private void validateAge(int age) {
        boolean isInvalidAge = (age < 18 || age > 65); // Example age limits
        throwExceptionIfTrue(isInvalidAge, "Age must be between 18 and 65.");
    }

    private void validateGender(String gender) {
        boolean isInvalidGender = (!"Male".equalsIgnoreCase(gender) &&
                !"Female".equalsIgnoreCase(gender) &&
                !"Other".equalsIgnoreCase(gender));
        throwExceptionIfTrue(isInvalidGender, "Invalid gender: " + gender);
    }

    private void validateSpecialty(String specialty) {
        boolean isInvalidSpecialty = (specialty == null || specialty.trim().isEmpty());
        throwExceptionIfTrue(isInvalidSpecialty, "Specialty cannot be empty.");
    }

    private void validateRoles(List<String> roles) {
        boolean isInvalidRoles = (roles == null || roles.isEmpty());
        throwExceptionIfTrue(isInvalidRoles, "Roles cannot be empty.");

        roles.forEach(role -> {
            boolean isInvalidRole = (role == null || role.trim().isEmpty());
            throwExceptionIfTrue(isInvalidRole, "Role cannot be empty.");
        });
    }

    private void throwExceptionIfTrue(boolean condition, String message) {
        if (condition) throw new IllegalArgumentException(message);
    }
}
