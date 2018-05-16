package com.killfomo.service.mapper;

import com.killfomo.domain.*;
import com.killfomo.service.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Component
public class TaskMapper implements EntityMapper<TaskDTO, Task> {



    @Autowired
    KillfomoJsonMapper killfomoJsonMapper;

    public Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }

    @Override
    public Task toEntity(TaskDTO dto) {
        return killfomoJsonMapper.convertValue(dto, Task.class);
    }

    @Override
    public TaskDTO toDto(Task entity) {
        return killfomoJsonMapper.convertValue(entity, TaskDTO.class);
    }

    @Override
    public List<Task> toEntity(List<TaskDTO> dtoList) {
        return dtoList.stream().map(a -> killfomoJsonMapper.convertValue(a, Task.class)).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> toDto(List<Task> entityList) {
        return entityList.stream().map(a -> killfomoJsonMapper.convertValue(a, TaskDTO.class)).collect(Collectors.toList());
    }
}
