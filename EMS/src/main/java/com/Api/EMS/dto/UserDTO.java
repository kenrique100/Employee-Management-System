package com.Api.EMS.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO<T> {
    private String companyName;
    private String name;
    private int age;
    private String gender;
    private String specialty;
    private String nationalIdNumber;
    private String dateOfEmployment;
    private String username;
    private String password;
    private List<T> roles;
}
