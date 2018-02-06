package edu.javacourse.contact.config;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class GlobalConfig
{
    private static final String CONFIG_NAME = "contacts.properties";
    private static final Properties GLOBAL_COFIG = new Properties();

    // Сделать начальную загрузку параметров из файла по умолчанию
    public static void initGlobalConfig() throws IOException {
        initGlobalConfig(null);
    }

    // Сделать загрузку данных из конфигурационного файла, имя которого передано в виде параметра
    // Если имя null или пустое - берем файл по умолчанию.
    public static void initGlobalConfig(String name) throws IOException {
        if (name != null && !name.trim().isEmpty()) {
            GLOBAL_COFIG.load(new FileReader(name));
        } else {
            GLOBAL_COFIG.load(new FileReader(CONFIG_NAME));
        }
    }

    // Получить значение параметра из глобальной конфигурации по имени
    public static String getProperty(String property) {
        return GLOBAL_COFIG.getProperty(property);
    }

    public static void createDB(){
        try {
            String drivers = System.getProperty("jdbc.drivers");
            if (drivers == null) {
                System.setProperty("jdbc.drivers", GLOBAL_COFIG.getProperty("db.driver.class"));
            }
            String url = GLOBAL_COFIG.getProperty("db.url");
            String login = GLOBAL_COFIG.getProperty("db.login");
            String password =GLOBAL_COFIG.getProperty("db.password");

            try (Connection con = DriverManager.getConnection(url, login, password);
                 Statement stmt = con.createStatement()){
                DatabaseMetaData databaseMetadata = con.getMetaData();
                ResultSet rs = databaseMetadata.getTables(null, null, "JC_CONTACT", null);
                if (rs.next()) {
                    stmt.addBatch("DROP TABLE JC_CONTACT");
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
                stmt.addBatch("INSERT INTO JC_CONTACT (FIRST_NAME, LAST_NAME, PHONE, EMAIL) VALUES ('Peter','Belgy','+79112345678','peter@pisem.net')");
                stmt.addBatch("INSERT INTO JC_CONTACT (FIRST_NAME, LAST_NAME, PHONE, EMAIL) VALUES ('Helga','Forte','+79118765432','helga@pisem.net')");
                stmt.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
