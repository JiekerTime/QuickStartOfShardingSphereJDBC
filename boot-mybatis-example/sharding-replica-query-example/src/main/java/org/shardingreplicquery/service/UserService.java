package org.shardingreplicquery.service;


import org.shardingreplicquery.domain.User;

import java.util.List;

public interface UserService {

    List<User> list();

    String saveUser();

    String deleteUser(Long id);
}