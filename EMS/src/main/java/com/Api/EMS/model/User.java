package com.Api.EMS.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String guid;
    private String name;
    private int age;
    private String gender;
    private String nationalIdNumber;
    private String dateOfEmployment;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User() {}
    // Specialty field
    private String specialty;

    // Builder class with specialty setter
    public static class UserBuilder {
        private String specialty;

        public UserBuilder specialty(String specialty) {
            this.specialty = specialty;
            return this;
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> (GrantedAuthority) () -> "ROLE_" + role).toList();
    }

    // UserDetails methods
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

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
