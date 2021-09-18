package org.example.service;

import org.example.domain.Role;

import java.util.List;

public interface RoleService {
    public List<Role> list();
    void save(Role role);
}
