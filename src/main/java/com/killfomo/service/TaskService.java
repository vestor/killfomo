package com.killfomo.service;

import com.killfomo.service.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Task.
 */
public interface TaskService {

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save
     * @return the persisted entity
     */
    TaskDTO save(TaskDTO taskDTO);

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" task.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TaskDTO findOne(String id);

    /**
     * Delete the "id" task.
     *
     * @param id the id of the entity
     */
    void delete(String id);


    /**
     * Get tasks by user id
     * @param userId
     * @param pageable
     * @return
     */
    Page<TaskDTO> findByUserId(Long userId, Pageable pageable);
}
