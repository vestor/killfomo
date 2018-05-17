package com.killfomo.repository;

import com.killfomo.domain.Task;
import com.killfomo.domain.enumeration.TaskState;
import com.killfomo.domain.enumeration.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    Page<Task> findByUserId(Long userId, Pageable pageable);

    List<String> findIdByTypeAndState(TaskType type, TaskState taskState);

    @Transactional
    @Modifying
    @Query("update Task t set t.state='DONE' where t.id NOT IN (:idList) and type = :taskType")
    void markAsDone(@Param("idList") List<String> idList, @Param("taskType")TaskType taskType);

}
