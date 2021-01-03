package org.shardingreplicquery.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.shardingreplicquery.domain.User;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * insert a lone of data
     *
     * @param record message of user
     * @author: Jieker
     **/
    int insert(User record);

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