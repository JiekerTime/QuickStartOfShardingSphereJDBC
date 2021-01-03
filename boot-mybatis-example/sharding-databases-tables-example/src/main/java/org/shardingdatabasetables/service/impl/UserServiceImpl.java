package org.shardingdatabasetables.service.impl;

import org.shardingdatabasetables.domain.User;
import org.shardingdatabasetables.mapper.UserMapper;
import org.shardingdatabasetables.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> list() {
        return userMapper.showAll();
    }

    @Override
    public String saveUser() {
        User user = new User();
        user.setName(UUID.randomUUID().toString().substring(0, 10));
        int randomValue = (int) (Math.random() * 100);
        user.setId((long) (Math.random() * 100000));
        user.setAge(randomValue);
        user.setSex(((randomValue % 2) == 1 ? "man" : "woman"));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insert(user);
        return " saved successfully ! ";
    }

    @Override
    public String deleteUser(Long id) {
        userMapper.deleteUser(id);
        return " deleted successfully ! ";
    }
}
