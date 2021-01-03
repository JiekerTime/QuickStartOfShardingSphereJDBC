package org.shardingtables.service;


import org.shardingtables.domain.User;

import java.util.List;

public interface UserService {

    List<User> list();

    String saveUser();

    String deleteUser(Long id);
}