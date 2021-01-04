package org.common.service.impl;


import org.common.domain.User;
import org.common.repository.UserRepository;
import org.common.repository.impl.UserRepositoryImpl;
import org.common.service.UserService;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(final DataSource dataSource) {
        this.userRepository=new UserRepositoryImpl(dataSource);
    }

    @Override
    public List<User> selectAll() {
        return userRepository.showAll();
    }

    @Override
    public String saveUser() {
        User user = new User();
        user.setName(UUID.randomUUID().toString().substring(0, 10));
        int randomValue = (int) (Math.random() * 100);
        user.setId( (long) (Math.random() * 10000));
        user.setAge(randomValue);
        user.setSex(((randomValue % 2) == 1 ? "man" : "woman"));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userRepository.insert(user);
        return " saved successfully ! ";
    }

    @Override
    public String deleteUser(Long id) {
        userRepository.deleteUser(id);
        return " deleted successfully ! ";
    }
}
