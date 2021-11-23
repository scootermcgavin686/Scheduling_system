package Model;

import DBAccess.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


/**
 * The Contacts class. This class will create contact objects by calling a select statement and setting the returned data
 * to local variable and creating a contact object. The objects will be saved into an Observable list called allContacts.
 */
public class Contacts {
    private int contact_ID;
    private String Contact_Name;
    private String Email;
    public static ObservableList<Contacts> allContacts = FXCollections.observableArrayList();

    /** The Contacts Constructor.
     * @param ContactID int type.
     * @param ContactName String type.
     * @param email String type.
     */
    public Contacts(int ContactID, String ContactName, String email) {
        this.contact_ID = ContactID;
        this.Contact_Name = ContactName;
        this.Email = email;
    }

    @Override
    /** The toString() method. This will return the Contact object as a string to be easily read in combo boxes or tables.*/
    public String toString() {
        return  Contact_Name;
    }

    /** The getDBContacts()  */
    public static void getDBContacts() {
        allContacts.clear();
        int dbContactID;
        String dbContactName;
        String dbEmail;

        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM contacts";

        try {
            statement.execute(selectStatement);
        } catch (SQLException error) {

        }

        try {
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                dbContactID = rs.getInt("contact_ID");
                dbContactName = rs.getString("Contact_Name");
                dbEmail = rs.getString("Email");
                Contacts newContact = new Contacts(dbContactID, dbContactName, dbEmail);
                allContacts.add(newContact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // This will get the observable list with all the contacts
    /** @return allContacts Observable list. */
    public static ObservableList<Contacts> getAllContacts() {
        return allContacts;
    }

    // Getters
    /** @return contact_ID Contacts ID variable. */
    public int getContact_ID() {
        return contact_ID;
    }

    /** @return Contact_Name Contacts name variable. String type.*/
    public String getContact_Name() {
        return Contact_Name;
    }

    public String getEmail() {return Email;}
}
