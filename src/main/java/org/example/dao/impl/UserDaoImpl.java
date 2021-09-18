package org.example.dao.impl;

import org.example.dao.UserDao;
import org.example.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(User user) {
        //创建PreparedStatementCreator
        PreparedStatementCreator creator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                //使用原始的JDBC完成 PrepareStatement的组建(PreparedStatement.RETURN_GENERATED_KEYS=1)
                PreparedStatement preparedStatement = connection.prepareStatement("insert into sys_user value(?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setObject(1,null);
                preparedStatement.setString(2,user.getUsername());
                preparedStatement.setString(3,user.getEmail());
                preparedStatement.setString(4,user.getPassword());
                preparedStatement.setString(5,user.getPhoneNum());
                return preparedStatement;
            }
        };
        //创建keyHolder
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(creator,keyHolder);

        //获得生成的主键
        long l = keyHolder.getKey().longValue();
        /*jdbcTemplate.update("insert into sys_user value(?,?,?,?,?)",null,user.getUsername(),user.getEmail(),user.getPassword(),user.getPhoneNum());*/
        return l;
    }

    @Override
    public void saveUserRoleRel(long id, Integer[] roleIds) {
        for(long roleId : roleIds){
            jdbcTemplate.update("insert into sys_user_role value(?,?)",id,roleId);
        }
    }

    @Override
    public void saveUserRel(long id) {
        jdbcTemplate.update("insert into sys_user_role value(?)",id);
    }

    @Override
    public List<User> findAll() {
        List<User> userList = jdbcTemplate.query("select * from sys_user", new BeanPropertyRowMapper<User>(User.class));
        return userList;
    }

    @Override
    public int delete(Long userId) {
        jdbcTemplate.update("delete from sys_user_role where userId=?",userId);
        int delete = jdbcTemplate.update("delete from sys_user where id= ?", userId);
        jdbcTemplate.execute("ALTER TABLE sys_user AUTO_INCREMENT = 1");
        return delete;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) throws EmptyResultDataAccessException {
        User user = jdbcTemplate.queryForObject("select * from sys_user where username=? and password=?", new BeanPropertyRowMapper<User>(User.class), username, password);
        return user;
    }


}
