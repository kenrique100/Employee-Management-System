package com.Api.EMS.service;

import com.Api.EMS.model.User;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<User> getUserById(String id);
}
