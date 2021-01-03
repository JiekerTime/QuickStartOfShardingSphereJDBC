package org.replicaquery.service;


import org.replicaquery.domain.User;

import java.util.List;

public interface UserService {

    List<User> list();

    String saveUser();

    String deleteUser(Long id);
}