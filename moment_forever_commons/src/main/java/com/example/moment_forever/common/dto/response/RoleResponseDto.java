package com.example.moment_forever.common.dto.response;

import java.time.LocalDateTime;

public class RoleResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer permissionLevel;
    private Boolean isActive;
    private Boolean isSystemRole;
    private LocalDateTime createdAt;
    private Integer userCount; // Optional: number of users with this role


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}