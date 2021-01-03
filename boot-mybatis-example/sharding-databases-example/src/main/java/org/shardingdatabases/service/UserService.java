package org.shardingdatabases.service;


import org.shardingdatabases.domain.User;

import java.util.List;

public interface UserService {

    List<User> list();

    String saveUser();

    String deleteUser(Long id);
}