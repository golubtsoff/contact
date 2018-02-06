package edu.javacourse.contact.exception;

public class ContactDaoException extends Exception
{
    public ContactDaoException() {
    }

    public ContactDaoException(String message) {
        super(message);
    }

    public ContactDaoException(Throwable cause) {
        super(cause);
    }

    public ContactDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
