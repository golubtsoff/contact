package edu.javacourse.contact.config;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class GlobalConfig {
    private static final String CONFIG_NAME = "contacts.properties";
    private static final String LOCALE_EN = "en";
    private static final String LOCALE_RU = "ru";
    private static final Properties GLOBAL_COFIG = new Properties();

    // Сделать начальную загрузку параметров из файла по умолчанию
    public static void initGlobalConfig() throws IOException {
        initGlobalConfig(null);
    }

    // Если язык выбран, то запускается приложение с хранением контактов в БД
    // в противном случае запускается демо-режим с хранением в памяти;
    // после перезапуска приложения все данные пропадают
    public static void initGlobalConfig(String locale) throws IOException {
        if (locale != null && !locale.trim().isEmpty() &&
             (locale.equalsIgnoreCase("-" + LOCALE_EN) || locale.equalsIgnoreCase("-" + LOCALE_RU))) {
            GLOBAL_COFIG.load(GlobalConfig.class.getClassLoader().getResourceAsStream(CONFIG_NAME));
            GLOBAL_COFIG.put("language", locale.replace("-",""));
            GLOBAL_COFIG.put("dao.class", "edu.javacourse.contact.dao.ContactDbDAO");
            createDB();
        } else {
            GLOBAL_COFIG.put("language", LOCALE_EN);
            GLOBAL_COFIG.put("dao.class", "edu.javacourse.contact.dao.ContactSimpleDAO");
        }
    }

    // Получить значение параметра из глобальной конфигурации по имени
    public static String getProperty(String property) {
        return GLOBAL_COFIG.getProperty(property);
    }

    private static void createDB() {
        try {
            String drivers = System.getProperty("jdbc.drivers");
            if (drivers == null) {
                System.setProperty("jdbc.drivers", GLOBAL_COFIG.getProperty("db.driver.class"));
            }
            String url = GLOBAL_COFIG.getProperty("db.url");
            String login = GLOBAL_COFIG.getProperty("db.login");
            String password = GLOBAL_COFIG.getProperty("db.password");

            try (Connection con = DriverManager.getConnection(url, login, password);
                 Statement stmt = con.createStatement()) {
                DatabaseMetaData databaseMetadata = con.getMetaData();
                ResultSet rs = databaseMetadata.getTables(null, null, "JC_CONTACT", null);
                if (rs.next()) {
                    return;
                }
                stmt.addBatch("CREATE TABLE JC_CONTACT" +
                        "(" +
                        "CONTACT_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "FIRST_NAME VARCHAR(50) NOT NULL," +
                        "LAST_NAME VARCHAR(50) NOT NULL," +
                        "PHONE VARCHAR(50) NOT NULL," +
                        "EMAIL VARCHAR(50) NOT NULL," +
                        "PRIMARY KEY (CONTACT_ID)" +
                        ")");
                stmt.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}