package edu.javacourse.contact.dao;

import edu.javacourse.contact.config.GlobalConfig;

/**
 * Фабрика для создания экземпляра ContactDAO
 */
public class ContactDAOFactory 
{   
    public static ContactDAO getContactDAO() {
        try {
            Class dao = Class.forName(GlobalConfig.getProperty("dao.class"));
            return (ContactDAO)dao.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
