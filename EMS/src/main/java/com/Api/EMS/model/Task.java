package com.Api.EMS.model;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import lombok.*;
import java.util.Date;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Date dueDate;
    private Date creationDate;
}
