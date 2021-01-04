package org.common.repository.impl;

import org.common.domain.User;
import org.common.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final DataSource DATASOURCE;

    public UserRepositoryImpl(final DataSource dataSource) {
        this.DATASOURCE = dataSource;
    }

    @Override
    public Long insert(User record) {
        String sql = "INSERT INTO tab_user (id, name, sex, age, create_time, update_time) " +
                "VALUES (?,?, ?, ?, ?, ?)";
        try (Connection connection = DATASOURCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, record.getId());
            preparedStatement.setString(2, record.getName());
            preparedStatement.setString(3, record.getSex());
            preparedStatement.setInt(4, record.getAge());
            preparedStatement.setDate(5, new Date(record.getCreateTime().getTime()));
            preparedStatement.setDate(6, new Date(record.getUpdateTime().getTime()));
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    record.setId(resultSet.getLong(1));
                }
            }
            return record.getId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return (long) -1;
        }
    }

    @Override
    public List<User> showAll() {
        String sql = "SELECT * FROM tab_user";
        List<User> users = new ArrayList<>();
        try (Connection connection = DATASOURCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setSex(resultSet.getString(3));
                user.setAge(resultSet.getInt(4));
                user.setCreateTime(resultSet.getDate(5));
                user.setUpdateTime(resultSet.getDate(6));
                users.add(user);
            }
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteUser(Long id) {
        String sql = "DELETE FROM tab_user WHERE id = ?";
        try (Connection connection = DATASOURCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
