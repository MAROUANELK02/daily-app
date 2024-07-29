package com.pfa.dailyapp.dtos;

import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTOResponse {
    private Long taskId;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTOResponse userDTO;
}
