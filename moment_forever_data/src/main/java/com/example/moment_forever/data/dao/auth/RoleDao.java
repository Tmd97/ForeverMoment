package com.example.moment_forever.data.dao.auth;

import com.example.moment_forever.data.dao.GenericDao;
import com.example.moment_forever.data.entities.auth.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao extends GenericDao<Role, Long> {

    Optional<Role> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Role> findByActiveTrue();

    List<Role> findBySystemRoleTrue();
    List<Role> getAllActiveRoles();

    List<Role> findByIds(List<Long> ids);



}