package edu.javacourse.contact.dao;

public class ConnectionBuilderFactory
{
    public static ConnectionBuilder getConnectionBuilder() {
        return new ComboConnectionBuilder();
    }
}
