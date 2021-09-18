package org.example.service;

import org.example.domain.User;

import java.util.List;

public interface UserService {
    public List<User> list();

    void save(User user, Integer[] roleIds);

    int delete(Long userId);

    User login(String username, String password);
}
