package com.example.moment_forever.common.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = false)
public class RoleRequestDto {

    @NotBlank(message = "Role name is required")
    @Size(min = 2, max = 50, message = "Role name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Role name must be uppercase with underscores (e.g., ADMIN, SUPER_ADMIN)")
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    private Integer permissionLevel = 10; // Default: 10 (USER level)

    private Boolean isActive = true;

    private Boolean isSystemRole = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(Integer permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getSystemRole() {
        return isSystemRole;
    }

    public void setSystemRole(Boolean systemRole) {
        isSystemRole = systemRole;
    }
}