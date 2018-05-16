package com.killfomo.service.impl;

import com.killfomo.domain.AuthResource;
import com.killfomo.domain.enumeration.TaskType;
import com.killfomo.repository.AuthResourceRepository;
import com.killfomo.service.AuthResourceService;
import com.killfomo.service.dto.AuthResourceDTO;
import com.killfomo.service.mapper.AuthResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AuthResource.
 */
@Service
@Transactional
public class AuthResourceServiceImpl implements AuthResourceService {

    private final Logger log = LoggerFactory.getLogger(AuthResourceServiceImpl.class);

    private final AuthResourceRepository authResourceRepository;

    private final AuthResourceMapper authResourceMapper;

    public AuthResourceServiceImpl(AuthResourceRepository authResourceRepository, AuthResourceMapper authResourceMapper) {
        this.authResourceRepository = authResourceRepository;
        this.authResourceMapper = authResourceMapper;
    }

    /**
     * Save a authResource.
     *
     * @param authResourceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AuthResourceDTO save(AuthResourceDTO authResourceDTO) {
        log.debug("Request to save AuthResource : {}", authResourceDTO);
        AuthResource authResource = authResourceMapper.toEntity(authResourceDTO);
        authResource = authResourceRepository.save(authResource);
        return authResourceMapper.toDto(authResource);
    }

    /**
     * Get all the authResources.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuthResourceDTO> findAll() {
        log.debug("Request to get all AuthResources");
        return authResourceRepository.findAll().stream()
            .map(authResourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one authResource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResourceDTO findOne(Long id) {
        log.debug("Request to get AuthResource : {}", id);
        AuthResource authResource = authResourceRepository.findOne(id);
        return authResourceMapper.toDto(authResource);
    }

    /**
     * Delete the authResource by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AuthResource : {}", id);
        authResourceRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResourceDTO findByUserIdAndType(Long userId, TaskType taskType) {
        log.debug("Request to get AuthResource for this userId {}", userId);
        AuthResource authResource = authResourceRepository.findByUserIdAndTaskType(userId, taskType);
        return authResourceMapper.toDto(authResource);
    }
}
