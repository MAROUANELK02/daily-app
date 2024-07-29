package com.pfa.dailyapp.mappers;

import com.pfa.dailyapp.dtos.TaskDTORequest;
import com.pfa.dailyapp.dtos.TaskDTOResponse;
import com.pfa.dailyapp.entities.Task;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskMapper {
    private UserMapper userMapper;

    public TaskDTOResponse toTaskDTO(Task task) {
        TaskDTOResponse taskDTOResponse = new TaskDTOResponse();
        BeanUtils.copyProperties(task, taskDTOResponse);
        taskDTOResponse.setUserDTO(userMapper.toUserDTO(task.getUser()));
        return taskDTOResponse;
    }

    public Task toTask(TaskDTOResponse taskDTOResponse) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTOResponse, task);
        task.setUser(userMapper.toUser(taskDTOResponse.getUserDTO()));
        return task;
    }

    public Task toTask(TaskDTORequest taskDTORequest) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTORequest, task);
        return task;
    }
}
