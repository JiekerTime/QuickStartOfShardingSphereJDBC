package org.shardingtables.config;


import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * The util for obtaining data sources
 */
public final class DataSourceUtil {

    private static final String HOST = "localhost";

    private static final int PORT = 3306;

    private static final String USER_NAME = "root";

    private static final String PASSWORD = "123456";

    public static DataSource createDataSource() {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setJdbcUrl(String.format("jdbc:mysql://%s:%s/ds1?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true", HOST, PORT));
        result.setUsername(USER_NAME);
        result.setPassword(PASSWORD);
        return result;
    }
}
