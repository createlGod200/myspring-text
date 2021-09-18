package org.example.dao;

import org.example.domain.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();

    long save(User user);

    void saveUserRoleRel(long id, Integer[] roleIds);

    void saveUserRel(long id);

    int delete(Long userId);

    User findByUsernameAndPassword(String username, String password);
}
