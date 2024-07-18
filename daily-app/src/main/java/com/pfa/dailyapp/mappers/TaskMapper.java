package com.pfa.dailyapp.mappers;

import com.pfa.dailyapp.dtos.TaskDTO;
import com.pfa.dailyapp.entities.Task;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskMapper {
    private UserMapper userMapper;

    public TaskDTO toTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(task, taskDTO);
        taskDTO.setUserDTO(userMapper.toUserDTO(task.getUser()));
        return taskDTO;
    }

    public Task toTask(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        task.setUser(userMapper.toUser(taskDTO.getUserDTO()));
        return task;
    }
}
