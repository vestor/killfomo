package com.killfomo.service.mapper;

import com.killfomo.domain.*;
import com.killfomo.service.dto.AuthResourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AuthResource and its DTO AuthResourceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthResourceMapper extends EntityMapper<AuthResourceDTO, AuthResource> {



    default AuthResource fromId(Long id) {
        if (id == null) {
            return null;
        }
        AuthResource authResource = new AuthResource();
        authResource.setId(id);
        return authResource;
    }
}
