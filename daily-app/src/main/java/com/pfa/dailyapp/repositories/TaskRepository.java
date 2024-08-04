package com.pfa.dailyapp.repositories;

import com.pfa.dailyapp.entities.Task;
import com.pfa.dailyapp.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.user.userId = :userId AND t.status = :status ORDER BY " +
            "CASE t.priority " +
            "WHEN com.pfa.dailyapp.enums.TaskPriority.HIGH THEN 1 " +
            "WHEN com.pfa.dailyapp.enums.TaskPriority.MEDIUM THEN 2 " +
            "WHEN com.pfa.dailyapp.enums.TaskPriority.LOW THEN 3 " +
            "END")
    Page<Task> findByUserUserIdAndStatusOrderByPriority(Long userId, TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN t.user u WHERE t.status = :status AND " +
            "(u.firstname LIKE %:keyword% OR u.lastname LIKE %:keyword%)")
    Page<Task> findAllByStatusAndUserKeyword(TaskStatus status, String keyword, Pageable pageable);

    List<Task> findAllByUser_UserIdAndStatus(Long userId, TaskStatus status);
    List<Task> findAllByUser_UserIdAndStatusAndUpdatedAtAfter(Long userId, TaskStatus status, LocalDateTime updatedAt);
}
