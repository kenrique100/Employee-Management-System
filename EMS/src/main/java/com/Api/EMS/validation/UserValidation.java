package com.Api.EMS.validation;

import com.Api.EMS.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final List<String> VALID_GENDERS = List.of("Male", "Female", "Other");

    public void validateUser(UserDTO<String> userDTO) {
        validateName(userDTO.getName());
        validateAge(userDTO.getAge());
        validateGender(userDTO.getGender());
        validateSpecialty(userDTO.getSpecialty());
        validateRoles(userDTO.getRoles());
    }

    private void validateName(String name) {
        throwExceptionIfTrue(isBlank(name) || !NAME_PATTERN.matcher(name).matches(),
                "Invalid name: " + name);
    }

    private void validateAge(int age) {
        throwExceptionIfTrue(age < 18 || age > 65,
                "Age must be between 18 and 65.");
    }

    private void validateGender(String gender) {
        throwExceptionIfTrue(isBlank(gender) || !VALID_GENDERS.contains(gender),
                "Invalid gender: " + gender);
    }

    private void validateSpecialty(String specialty) {
        throwExceptionIfTrue(isBlank(specialty),
                "Specialty cannot be empty.");
    }

    private void validateRoles(List<String> roles) {
        throwExceptionIfTrue(roles == null || roles.isEmpty(),
                "Roles cannot be empty.");
        roles.forEach(role -> throwExceptionIfTrue(isBlank(role),
                "Role cannot be empty."));
    }

    // Helper methods for reuse and readability
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void throwExceptionIfTrue(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
