package edu.javacourse.contact.dao;

import edu.javacourse.contact.entity.Contact;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ContactSimpleDAO implements ContactDAO
{
    private final List<Contact> contacts = new ArrayList<>();

    // Прямо в конструкторе добавляем три контакта
    public ContactSimpleDAO() {
        addContact(new Contact("Андрей", "Соколов", "+7-911-890-7766", "sokolov@yandex.ru"));
        addContact(new Contact("Сергей", "Иванов", "+7-911-890-7755", "ivanov@google.com"));
        addContact(new Contact("Татьяна", "Семенова", "+7-911-890-1164", "semenova@mail.ru"));
    }
    
    @Override
    public Long addContact(Contact contact) {
        Long id = generateContactId();
        contact.setContactId(id);
        contacts.add(contact);
        return id;
    }

    @Override
    public void updateContact(Contact contact) {
        Contact oldContact = getContact(contact.getContactId());
        if(oldContact != null) {
            oldContact.setFirstName(contact.getFirstName());
            oldContact.setLastName(contact.getLastName());
            oldContact.setPhone(contact.getPhone());
            oldContact.setEmail(contact.getEmail());
        }
    }

    @Override
    public void deleteContact(Long contactId) {
        for(Iterator<Contact> it = contacts.iterator(); it.hasNext();) {
            Contact cnt = it.next();
            if(cnt.getContactId().equals(contactId)) {
                it.remove();
            }
        }
    }

    @Override
    public Contact getContact(Long contactId) {
        for(Contact contact : contacts) {
            if(contact.getContactId().equals(contactId)) {
                return contact;
            }
        }
        return null;
    }
    
    @Override
    public List<Contact> findContacts() {
        return contacts;
    }

    private Long generateContactId() {
        Long contactId = Math.round(Math.random() * 1000 + System.currentTimeMillis());
        while(getContact(contactId) != null) {
            contactId = Math.round(Math.random() * 1000 + System.currentTimeMillis());
        }
        return contactId;
    }
}
