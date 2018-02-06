package edu.javacourse.contact.dao;

import edu.javacourse.contact.entity.Contact;
import edu.javacourse.contact.exception.ContactDaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ContactDbDAO implements ContactDAO
{
    private static final String SELECT
            = "SELECT contact_id, first_name, last_name, phone, email FROM jc_contact ORDER BY first_name, last_name";
    private static final String SELECT_ONE
            = "SELECT contact_id, first_name, last_name, phone, email FROM jc_contact WHERE contact_id=?";
    private static final String INSERT
            = "INSERT INTO jc_contact (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
    private static final String UPDATE
            = "UPDATE jc_contact SET first_name=?, last_name=?, phone=?, email=? WHERE contact_id=?";
    private static final String DELETE
            = "DELETE FROM jc_contact WHERE contact_id=?";

    private ConnectionBuilder builder = ConnectionBuilderFactory.getConnectionBuilder();

    private Connection getConnection() throws SQLException {
        return builder.getConnection();
    }

    @Override
    public Long addContact(Contact contact) throws ContactDaoException {
        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(INSERT, new String[]{"contact_id"})) {
            Long contactId = -1L;
            pst.setString(1, contact.getFirstName());
            pst.setString(2, contact.getLastName());
            pst.setString(3, contact.getPhone());
            pst.setString(4, contact.getEmail());
            pst.executeUpdate();
            ResultSet gk = pst.getGeneratedKeys();
            if (gk.next()) {
                contactId = gk.getLong("contact_id");
            }
            gk.close();
            return contactId;
        } catch (Exception e) {
            throw new ContactDaoException(e);
        }
    }

    @Override
    public void updateContact(Contact contact) throws ContactDaoException {
        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(UPDATE)) {
            pst.setString(1, contact.getFirstName());
            pst.setString(2, contact.getLastName());
            pst.setString(3, contact.getPhone());
            pst.setString(4, contact.getEmail());
            pst.setLong(5, contact.getContactId());
            pst.executeUpdate();
        } catch (Exception e) {
            throw new ContactDaoException(e);
        }
    }

    @Override
    public void deleteContact(Long contactId) throws ContactDaoException {
        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(DELETE)) {
            pst.setLong(1, contactId);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new ContactDaoException(e);
        }
    }

    @Override
    public Contact getContact(Long contactId) throws ContactDaoException {
        Contact contact = null;
        try (Connection con = getConnection()) {
            PreparedStatement pst = con.prepareStatement(SELECT_ONE);
            pst.setLong(1, contactId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                contact = fillContact(rs);
            }
            rs.close();
            pst.close();
        } catch (Exception e) {
            throw new ContactDaoException(e);
        }
        return contact;
    }

    @Override
    public List<Contact> findContacts() throws ContactDaoException {
        List<Contact> list = new LinkedList<>();
        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(SELECT);
                ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(fillContact(rs));
            }
            rs.close();
        } catch (Exception e) {
            throw new ContactDaoException(e);
        }
        return list;
    }

    private Contact fillContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setContactId(rs.getLong("contact_id"));
        contact.setFirstName(rs.getString("first_name"));
        contact.setLastName(rs.getString("last_name"));
        contact.setPhone(rs.getString("phone"));
        contact.setEmail(rs.getString("email"));
        return contact;
    }
}
