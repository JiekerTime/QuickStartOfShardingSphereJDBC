package org.common.service;


import org.common.domain.User;

import java.util.List;

public interface UserService {

    List<User> selectAll();

    String saveUser();

    String deleteUser(Long id);
}