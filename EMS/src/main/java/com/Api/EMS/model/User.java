package com.Api.EMS.model;

import com.Api.EMS.security.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String guid;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(18)
    @Max(80)
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender; // 'male' or 'female'

    @NotBlank(message = "Specialty is required")
    private String specialty;

    @NotNull
    private LocalDate dateOfEmployment;

    private String profilePictureUrl;

    private String documentUrl;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
