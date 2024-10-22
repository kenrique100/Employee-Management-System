package com.Api.EMS.validation;

import com.Api.EMS.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    // Regex pattern for valid names (only letters and spaces)
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    public void validateUser(UserDTO userDTO) {
        validateName(userDTO.getName());
        validateAge(userDTO.getAge());
        validateGender(userDTO.getGender());
        validateSpecialty(userDTO.getSpecialty());
        validateRoles(userDTO.getRoles());
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }
    }

    private void validateAge(int age) {
        if (age < 18 || age > 65) { // Example age limits
            throw new IllegalArgumentException("Age must be between 18 and 65.");
        }
    }

    private void validateGender(String gender) {
        if (!"Male".equalsIgnoreCase(gender) && !"Female".equalsIgnoreCase(gender) && !"Other".equalsIgnoreCase(gender)) {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }

    private void validateSpecialty(String specialty) {
        if (specialty == null || specialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty cannot be empty.");
        }
    }

    private void validateRoles(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be empty.");
        }
        for (String role : roles) {
            if (role == null || role.trim().isEmpty()) {
                throw new IllegalArgumentException("Role cannot be empty.");
            }
        }
    }
}
