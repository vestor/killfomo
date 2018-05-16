package com.killfomo.repository;

import com.killfomo.domain.AuthResource;
import com.killfomo.domain.enumeration.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the AuthResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthResourceRepository extends JpaRepository<AuthResource, Long> {

    AuthResource findByUserIdAndType(Long userId, TaskType taskType);

}
