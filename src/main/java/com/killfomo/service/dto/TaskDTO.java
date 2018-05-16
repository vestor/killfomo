package com.killfomo.service.dto;


import com.killfomo.domain.enumeration.TaskType;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Task entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Lob
    private String subject;

    @Lob
    private String externalLink;

    @NotNull
    private TaskType type;

    private Instant dueBy;

    @NotNull
    private Instant externalCreatedAt;

    @Lob
    private String customJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Instant getDueBy() {
        return dueBy;
    }

    public void setDueBy(Instant dueBy) {
        this.dueBy = dueBy;
    }

    public Instant getExternalCreatedAt() {
        return externalCreatedAt;
    }

    public void setExternalCreatedAt(Instant externalCreatedAt) {
        this.externalCreatedAt = externalCreatedAt;
    }

    public String getCustomJson() {
        return customJson;
    }

    public void setCustomJson(String customJson) {
        this.customJson = customJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if(taskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", subject='" + getSubject() + "'" +
            ", externalLink='" + getExternalLink() + "'" +
            ", type='" + getType() + "'" +
            ", dueBy='" + getDueBy() + "'" +
            ", externalCreatedAt='" + getExternalCreatedAt() + "'" +
            ", customJson='" + getCustomJson() + "'" +
            "}";
    }
}
