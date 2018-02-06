package edu.javacourse.contact.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionBuilder
{
    Connection getConnection() throws SQLException;
}
