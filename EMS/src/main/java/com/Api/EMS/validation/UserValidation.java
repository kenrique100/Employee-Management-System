package com.Api.EMS.validation;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    public void validateUser(UserDTO<String> userDTO) {
        validateName(userDTO.getName());
        validateDateOfBirth(userDTO.getDateOfBirth());
        validateAge(userDTO.getAge());
        validateGender(userDTO.getGender());
        validateSpecialty(userDTO.getSpecialty());
        validateRoles(userDTO.getRoles());
    }

    private void validateName(String name) {
        boolean isInvalidName = (name == null || name.trim().isEmpty() || !NAME_PATTERN.matcher(name).matches());
        throwExceptionIfTrue(isInvalidName, "Invalid name: " + name);
    }

    private void validateDateOfBirth(int dateOfBirth) {
        boolean isInvalidDateOfBirth = (dateOfBirth < 1945 || dateOfBirth > 2007);
        throwExceptionIfTrue(isInvalidDateOfBirth, "Date of birth must be between 1944 and 2006.");
    }


    private void validateAge(int age) {
        boolean isInvalidAge = (age < 18 || age > 80);
        throwExceptionIfTrue(isInvalidAge, "Age must be between 18 and 80.");
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

    private void validateRoles(List<Role> roles) {
        boolean isInvalidRoles = (roles == null || roles.isEmpty());
        throwExceptionIfTrue(isInvalidRoles, "Roles cannot be empty.");
    }


    private void throwExceptionIfTrue(boolean condition, String message) {
        if (condition) throw new IllegalArgumentException(message);
    }
}
