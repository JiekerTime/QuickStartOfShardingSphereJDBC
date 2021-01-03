package org.shardingdatabasetables.service;


import org.shardingdatabasetables.domain.User;

import java.util.List;

public interface UserService {

    List<User> list();

    String saveUser();

    String deleteUser(Long id);
}