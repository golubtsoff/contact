package edu.javacourse.contact.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.javacourse.contact.config.GlobalConfig;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class ComboConnectionBuilder implements ConnectionBuilder
{
    private ComboPooledDataSource dataSource;

    public ComboConnectionBuilder() {
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(GlobalConfig.getProperty("db.driver.class"));
            dataSource.setJdbcUrl(GlobalConfig.getProperty("db.url"));
            dataSource.setUser(GlobalConfig.getProperty("db.login"));
            dataSource.setPassword(GlobalConfig.getProperty("db.password"));
            dataSource.setMaxPoolSize(20);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
