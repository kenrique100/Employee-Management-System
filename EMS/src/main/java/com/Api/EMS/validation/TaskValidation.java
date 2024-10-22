package com.Api.EMS.validation;

import com.Api.EMS.model.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskValidation {

    public void validateTask(Task task) {
        validateTitle(task.getTitle());
        validateDescription(task.getDescription());
        validateDueDate(task.getDueDate());
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
    }

    private void validateDueDate(LocalDate dueDate) {
        if (dueDate == null || dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past.");
        }
    }
}
