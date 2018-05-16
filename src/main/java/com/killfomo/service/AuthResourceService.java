package com.killfomo.service;

import com.killfomo.domain.enumeration.TaskType;
import com.killfomo.service.dto.AuthResourceDTO;

import java.util.List;

/**
 * Service Interface for managing AuthResource.
 */
public interface AuthResourceService {

    /**
     * Save a authResource.
     *
     * @param authResourceDTO the entity to save
     * @return the persisted entity
     */
    AuthResourceDTO save(AuthResourceDTO authResourceDTO);

    /**
     * Get all the authResources.
     *
     * @return the list of entities
     */
    List<AuthResourceDTO> findAll();

    /**
     * Get the "id" authResource.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AuthResourceDTO findOne(Long id);

    /**
     * Delete the "id" authResource.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get the authResource by userId
     *
     * @return the Entity
     */
    AuthResourceDTO findByUserIdAndType(Long userId, TaskType taskType);
}
