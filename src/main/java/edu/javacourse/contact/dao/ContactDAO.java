package edu.javacourse.contact.dao;

import edu.javacourse.contact.entity.Contact;
import edu.javacourse.contact.exception.ContactDaoException;
import java.util.List;

/**
 * Интерфейс для определения функций хранлиза данных о контактах
 */
public interface ContactDAO 
{
    // Добавление контакта - возвращает ID добавленного контакта
    public Long addContact(Contact contact) throws ContactDaoException;
    // Редактирование контакта
    public void updateContact(Contact contact) throws ContactDaoException;
    // Удаление контакта по его ID
    public void deleteContact(Long contactId) throws ContactDaoException;
    // Получение контакта
    public Contact getContact(Long contactId) throws ContactDaoException;
    // Получение списка контактов
    public List<Contact> findContacts() throws ContactDaoException;
    
}
