package com.pfa.dailyapp.repositories;

import com.pfa.dailyapp.entities.Task;
import com.pfa.dailyapp.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    Page<Task> findByUserIdAndStatusOrderByPriority(Long userId, TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.user.userId = :userId AND t.status = :status ORDER BY " +
            "FUNCTION('DATE', t.updatedAt) DESC, " +
            "CASE t.priority " +
            "WHEN com.pfa.dailyapp.enums.TaskPriority.HIGH THEN 1 " +
            "WHEN com.pfa.dailyapp.enums.TaskPriority.MEDIUM THEN 2 " +
            "WHEN com.pfa.dailyapp.enums.TaskPriority.LOW THEN 3 " +
            "END")
    Page<Task> findByUserIdAndStatusOrderByPriorityAndDate(Long userId, TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN t.user u WHERE t.status = :status AND " +
            "(u.firstname LIKE %:keyword% OR u.lastname LIKE %:keyword%)")
    Page<Task> findAllByStatusAndUserKeyword(TaskStatus status, String keyword, Pageable pageable);

    List<Task> findAllByUser_UserIdAndStatus(Long userId, TaskStatus status);

    List<Task> findAllByUser_UserIdAndStatusAndUpdatedAtAfter(Long userId, TaskStatus status, LocalDateTime updatedAt);

    @Modifying
    @Query("DELETE FROM Task t WHERE t.user.userId = :userId")
    void deleteAllTasksByUser_UserId(Long userId);

    @Query("SELECT FUNCTION('DATE', t.updatedAt), COUNT(t) " +
            "FROM Task t " +
            "WHERE t.user.userId = :userId " +
            "AND t.status = 'DONE' " +
            "AND t.updatedAt >= :startDate " +
            "GROUP BY FUNCTION('DATE', t.updatedAt) " +
            "ORDER BY FUNCTION('DATE', t.updatedAt) ASC")
    List<Object[]> findCompletedTasksCountPerDayByUserId(Long userId, LocalDateTime startDate);

    @Query("SELECT FUNCTION('DATE', t.createdAt), COUNT(t) " +
            "FROM Task t " +
            "WHERE t.user.userId = :userId " +
            "AND t.status = 'IN_PROGRESS' " +
            "AND t.createdAt >= :startDate " +
            "GROUP BY FUNCTION('DATE', t.createdAt) " +
            "ORDER BY FUNCTION('DATE', t.createdAt) ASC")
    List<Object[]> findInProgressTasksCountPerDayByUserId(Long userId, LocalDateTime startDate);
}
