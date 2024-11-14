package com.Api.EMS.dto;

import com.Api.EMS.model.Role;
import lombok.Data;
import java.util.List;

@Data
public class UserDTO<String> {
    private String guid;
    private String username;
    private String password;
    private List<Role> roles;
    private String name;
    private int dateOfBirth;
    private int age;
    private String telephoneNumber;
    private String gender;
    private String email;
    private String nationalIdNumber;
    private String dateOfEmployment;
    private String specialty;
}
