package org.shardingdatabases;

import org.common.domain.User;
import org.common.service.UserService;
import org.common.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.shardingdatabase.config.ShardingDatabasesConfig;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author : Jieker
 * @Description :
 * @date : 2021/1/4 10:57
 */
public class ShardingDatabasesAPITest {


    private UserService userService;

    @Before
    public void init() throws IOException, SQLException {
        DataSource dataSource = ShardingDatabasesConfig.getDataSource();
        userService = new UserServiceImpl(dataSource);
    }

    @Test
    public void selectAllTest() {
        List<User> users = userService.selectAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void insertTest() {
        userService.saveUser();
    }

    @Test
    public void deleteTest() {
        userService.deleteUser((long) 7);
    }

}
