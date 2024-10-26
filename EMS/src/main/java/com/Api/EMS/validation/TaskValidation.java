package com.Api.EMS.validation;

import com.Api.EMS.model.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TaskValidation {

    public void validateTask(Task task) {
        validateTitle(task.getTitle());
        validateDescription(task.getDescription());
        validateDueDate(convertToLocalDate(task.getDueDate()));
    }

    private void validateTitle(String title) {
        boolean isInvalidTitle = (title == null || title.trim().isEmpty());
        throwExceptionIfTrue(isInvalidTitle, "Task title cannot be empty.");
    }

    private void validateDescription(String description) {
        boolean isInvalidDescription = (description == null || description.trim().isEmpty());
        throwExceptionIfTrue(isInvalidDescription, "Task description cannot be empty.");
    }

    private void validateDueDate(LocalDate dueDate) {
        boolean isInvalidDueDate = (dueDate == null || dueDate.isBefore(LocalDate.now()));
        throwExceptionIfTrue(isInvalidDueDate, "Due date cannot be in the past.");
    }

    private LocalDate convertToLocalDate(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void throwExceptionIfTrue(boolean condition, String message) {
        if (condition) throw new IllegalArgumentException(message);
    }
}
