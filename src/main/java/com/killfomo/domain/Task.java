package com.killfomo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.killfomo.domain.enumeration.TaskType;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "external_link")
    private String externalLink;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TaskType type;

    @Column(name = "due_by")
    private Instant dueBy;

    @Column(name = "external_created_at")
    private Instant externalCreatedAt;

    @Column(name = "custom_json")
    private String customJson;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Task userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public Task subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public Task externalLink(String externalLink) {
        this.externalLink = externalLink;
        return this;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public TaskType getType() {
        return type;
    }

    public Task type(TaskType type) {
        this.type = type;
        return this;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Instant getDueBy() {
        return dueBy;
    }

    public Task dueBy(Instant dueBy) {
        this.dueBy = dueBy;
        return this;
    }

    public void setDueBy(Instant dueBy) {
        this.dueBy = dueBy;
    }

    public Instant getExternalCreatedAt() {
        return externalCreatedAt;
    }

    public Task externalCreatedAt(Instant externalCreatedAt) {
        this.externalCreatedAt = externalCreatedAt;
        return this;
    }

    public void setExternalCreatedAt(Instant externalCreatedAt) {
        this.externalCreatedAt = externalCreatedAt;
    }

    public String getCustomJson() {
        return customJson;
    }

    public Task customJson(String customJson) {
        this.customJson = customJson;
        return this;
    }

    public void setCustomJson(String customJson) {
        this.customJson = customJson;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        if (task.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Task{" +
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
