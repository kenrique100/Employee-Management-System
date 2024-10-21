package com.Api.EMS.dto;

import com.Api.EMS.security.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String guid;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(18)
    @Max(80)
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    private LocalDate dateOfEmployment;

    private String profilePictureUrl;
    private String documentUrl;

    private Set<Role> roles;
}
