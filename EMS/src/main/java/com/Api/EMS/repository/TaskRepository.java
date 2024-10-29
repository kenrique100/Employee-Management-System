package com.Api.EMS.repository;

import com.Api.EMS.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> { // Use String for MongoDB ObjectId
    // Additional custom query methods can be added here if needed
}
