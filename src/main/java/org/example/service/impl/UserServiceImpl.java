package org.example.service.impl;

import org.example.dao.RoleDao;
import org.example.dao.UserDao;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Override
    public List<User> list() {
        List<User> userList = userDao.findAll();
        //封装userList中的每一个User的Role属性
        for(User user:userList){
            //获得User的id
            long id = user.getId();
            //将id作为参数 查询当前userId对应的Role集合数据
            List<Role> roles = roleDao.findRoleByUserId(id);
            user.setRoles(roles);
        }
        return userList;
    }

    @Override
    public void save(User user, Integer[] roleIds) {
        //第一步 向sys_user表中存储数据
        long id = userDao.save(user);
        //第二步 向sys_user_role关系表中存储多条数据
        if(roleIds!=null){
            userDao.saveUserRoleRel(id,roleIds);
        }else{
            userDao.saveUserRel(id);
        }
    }

    @Override
    public int delete(Long userId) {
        int delete = userDao.delete(userId);
        return delete;
    }

    @Override
    public User login(String username, String password) {
        try {
            User user = userDao.findByUsernameAndPassword(username, password);
            return user;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
