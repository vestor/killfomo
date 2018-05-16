package com.killfomo.service.mapper;

import com.killfomo.domain.AuthResource;
import com.killfomo.service.dto.AuthResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity AuthResource and its DTO AuthResourceDTO.
 */
//@Mapper(componentModel = "spring", uses = {})
@Component
public class AuthResourceMapper implements EntityMapper<AuthResourceDTO,AuthResource> {

    @Autowired
    KillfomoJsonMapper killfomoJsonMapper;


    public AuthResource fromId(Long id) {
        if (id == null) {
            return null;
        }
        AuthResource authResource = new AuthResource();
        authResource.setId(id);
        return authResource;
    }

    @Override
    public AuthResource toEntity(AuthResourceDTO dto) {
        return killfomoJsonMapper.convertValue(dto, AuthResource.class);
    }

    @Override
    public AuthResourceDTO toDto(AuthResource entity) {
        return killfomoJsonMapper.convertValue(entity, AuthResourceDTO.class);
    }

    @Override
    public List<AuthResource> toEntity(List<AuthResourceDTO> dtoList) {
        return dtoList.stream().map(a -> killfomoJsonMapper.convertValue(a, AuthResource.class)).collect(Collectors.toList());
    }

    @Override
    public List<AuthResourceDTO> toDto(List<AuthResource> entityList) {
        return entityList.stream().map(a -> killfomoJsonMapper.convertValue(a, AuthResourceDTO.class)).collect(Collectors.toList());
    }
}
