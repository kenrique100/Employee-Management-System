package com.Api.EMS.service;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
}
