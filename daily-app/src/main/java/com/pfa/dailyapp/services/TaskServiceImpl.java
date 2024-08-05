package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.TaskDTORequest;
import com.pfa.dailyapp.dtos.TaskDTOResponse;
import com.pfa.dailyapp.entities.Task;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.mappers.TaskMapper;
import com.pfa.dailyapp.mappers.UserMapper;
import com.pfa.dailyapp.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserMapper userMapper;
    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private UserService userService;

    @Override
    public Page<TaskDTOResponse> getTasksByUserId(Long userId, TaskStatus status, int page, int size) {
        log.info("Fetching tasks for user with id: {}", userId);
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Task> tasks = taskRepository.findByUserUserIdAndStatusOrderByPriority(userId, status, pageable);
        return tasks.map(taskMapper::toTaskDTO);
    }

    @Override
    public TaskDTOResponse getTaskById(Long id) throws TaskNotFoundException {
        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée"));
        return taskMapper.toTaskDTO(task);
    }

    @Override
    public Long getUserIdByTaskId(Long taskId) throws TaskNotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée"));
        return task.getUser().getUserId();
    }

    @Override
    public Page<TaskDTOResponse> getTasksHistory(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"updatedAt"));
        Page<Task> tasks = taskRepository.findAllByStatusAndUserKeyword(TaskStatus.DONE, keyword, pageable);
        return tasks.map(taskMapper::toTaskDTO);
    }

    @Override
    public TaskDTOResponse saveTask(TaskDTORequest task, Long userId) {
        log.info("Saving task: {}", task);
        Task task1 = taskMapper.toTask(task);
        try {
            task1.setUser(userMapper.toUser(userService.getUserById(userId)));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task1);
        try {
            userService.incrementTasksCount(userId);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        log.info("Task saved successfully");
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public TaskDTOResponse updateTask(TaskDTORequest task, Long taskId) throws TaskNotFoundException {
        log.info("Updating task: {}", task);
        Task task1 = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée"));
        if (!task1.getTitle().equals(task.getTitle()))
            task1.setTitle(task.getTitle());
        if (!task1.getDescription().equals(task.getDescription()))
            task1.setDescription(task.getDescription());
        task1.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task1);
        log.info("Task updated successfully");
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public TaskDTOResponse changeTaskStatus(TaskStatus status, Long id) throws TaskNotFoundException, UserNotFoundException {
        log.info("Changing task status to: {}", status);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée"));
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task);
        if(status.equals(TaskStatus.DONE))
            userService.decrementTasksCount(save.getUser().getUserId());
        else if(status.equals(TaskStatus.IN_PROGRESS))
            userService.incrementTasksCount(save.getUser().getUserId());
        log.info("Task status changed successfully");
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public TaskDTOResponse changeTaskPriority(TaskPriority priority, Long id) throws TaskNotFoundException {
        log.info("Changing task priority to: {}", priority);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée"));
        task.setPriority(priority);
        task.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task);
        log.info("Task priority changed successfully");
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException, UserNotFoundException {
        log.info("Deleting task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Tâche non trouvée"));
        Long userId = task.getUser().getUserId();
        taskRepository.deleteById(id);
        if(task.getStatus().equals(TaskStatus.IN_PROGRESS))
            userService.decrementTasksCount(userId);
        log.info("Task deleted successfully");
    }
}
