package com.Api.EMS.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String guid;
    private String username;
    private String password;
    private List<Role> roles;  // Changed from List<String> to List<Role>
    private String name;
    private int age;
    private String gender;
    private String nationalIdNumber;
    private String dateOfEmployment;
    private String specialty;
    private String companyName;

    // Convert roles into authorities for Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.name())  // Convert Role to ROLE_ prefix
                .collect(Collectors.toList());
    }

    // Override methods from UserDetails
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
