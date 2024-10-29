package com.Api.EMS.model;

import lombok.*; // Lombok annotations for generating boilerplate code
import org.springframework.data.annotation.Id; // MongoDB ID annotation
import org.springframework.data.mongodb.core.mapping.Document; // Indicates this class is a MongoDB document
import org.springframework.security.core.GrantedAuthority; // Interface for granted authority
import org.springframework.security.core.userdetails.UserDetails; // Interface for user details in Spring Security

import java.util.Collection; // For collection of granted authorities
import java.util.List; // List for roles

@Document(collection = "users") // Defines the MongoDB collection name
@Data // Lombok annotation to generate getters, setters, toString, etc.
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@Builder // Lombok annotation to provide a builder pattern for this class
public class User implements UserDetails {

    @Id
    private String id;  // MongoDB ID field, typically a String

    private String username; // Username field
    private String password; // Password field
    private String guid; // Unique identifier for the user
    private String name; // User's name
    private int age; // User's age
    private String gender; // User's gender
    private String nationalIdNumber; // User's national ID
    private String dateOfEmployment; // Date of employment
    private List<String> roles; // User's roles, stored as a list

    private String specialty; // Additional field for user specialization

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert roles to GrantedAuthority
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role) // Each role is prefixed with "ROLE_"
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // User account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // User account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // User credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // User account is enabled
    }
}
