package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.utils.AuthValidationUtil;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ResponseUtil responseUtil;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupAdmin(@RequestBody AuthRequest authRequest) {
        ResponseEntity<AuthResponse> validationResponse = AuthValidationUtil.validateAdminRole(authRequest, responseUtil);
        return validationResponse != null ? validationResponse : ResponseEntity.ok(adminService.signup(authRequest));
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody UserDTO<String> userDTO) {
        try {
            User user = adminService.createUser(userDTO);
            return responseUtil.createSuccessResponse(user);
        } catch (Exception e) {
            return responseUtil.createErrorResponse(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return responseUtil.createSuccessResponse(adminService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return adminService.findUserById(id)
                .map(responseUtil::createSuccessResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO<String> userDTO) {
        return adminService.updateUser(id, userDTO)
                .map(responseUtil::createSuccessResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return adminService.deleteUser(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
