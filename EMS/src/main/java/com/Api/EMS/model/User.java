package com.Api.EMS.model;

import com.Api.EMS.dto.UserDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String guid;
    private String username;
    private String password;
    private List<Role> roles;
    private String name;
    private int dateOfBirth;
    private int age;
    private String telephoneNumber;
    private String gender;
    private String nationalIdNumber;
    private String dateOfEmployment;
    private String specialty;
    private String email;

    public void updateFromDTO(UserDTO<String> userDTO) {
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.roles = userDTO.getRoles();
        this.name = userDTO.getName();
        this.dateOfBirth = userDTO.getDateOfBirth();
        this.age = userDTO.getAge();
        this.telephoneNumber = userDTO.getTelephoneNumber();
        this.gender = userDTO.getGender();
        this.nationalIdNumber = userDTO.getNationalIdNumber();
        this.dateOfEmployment = userDTO.getDateOfEmployment();
        this.specialty = userDTO.getSpecialty();
        this.email = userDTO.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.name())
                .collect(Collectors.toList());
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
