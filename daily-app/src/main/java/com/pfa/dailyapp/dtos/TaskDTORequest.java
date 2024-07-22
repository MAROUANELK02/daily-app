package com.pfa.dailyapp.dtos;

import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTORequest {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
}
