package com.killfomo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.killfomo.domain.enumeration.TaskType;

/**
 * A DTO for the AuthResource entity.
 */
public class AuthResourceDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    private String token;

    @NotNull
    private TaskType type;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthResourceDTO authResourceDTO = (AuthResourceDTO) o;
        if(authResourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authResourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuthResourceDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", token='" + getToken() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
