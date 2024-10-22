package com.Api.EMS.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String name;
    private int age;
    private String gender;
    private String specialty;
    private String nationalIdNumber;
    private String dateOfEmployment;
    private String username;
    private String password;
    private List<String> roles;
}
