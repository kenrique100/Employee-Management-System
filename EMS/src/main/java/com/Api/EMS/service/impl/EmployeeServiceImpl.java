package com.Api.EMS.service.impl;

import com.Api.EMS.exception.ResourceNotFoundException;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id) // This returns Mono<User>
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with id: " + id)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}