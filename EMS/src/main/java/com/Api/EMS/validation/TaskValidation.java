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

        // Convert java.util.Date to java.time.LocalDate
        LocalDate dueDate = convertToLocalDate(task.getDueDate());
        validateDueDate(dueDate);
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

    // Utility method to convert java.util.Date to java.time.LocalDate
    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
