package org.common.repository;


import org.common.domain.User;

import java.util.List;

public interface UserRepository {

    /**
     * insert a lone of data
     *
     * @param record message of user
     * @author: Jieker
     *
     * @return*/
    Long insert(User record);

    /**
     * select all of data
     *
     * @author: Jieker
     **/
    List<User> showAll();

    /**
     * delete a user
     *
     * @param id userId
     * @author: Jieker
     **/
    void deleteUser(Long id);
}