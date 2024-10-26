package com.Api.EMS.controller;

import com.Api.EMS.model.Task;
import com.Api.EMS.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Task>> createTask(@RequestBody Task task) {
        return taskRepository.save(task)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Task>> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskRepository.findById(id)
                .flatMap(existingTask -> {
                    existingTask.setDueDate(task.getDueDate());
                    existingTask.setDescription(task.getDescription());
                    return taskRepository.save(existingTask);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .flatMap(existingTask ->
                        taskRepository.delete(existingTask).then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public Flux<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
