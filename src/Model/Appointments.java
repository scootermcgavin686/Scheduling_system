package Model;

import DBAccess.DBQuery;
import DBAccess.TimeConversion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static DBAccess.DBConnect.conn;
import java.sql.*;
import java.time.LocalDateTime;

/** The Appointments class. This class will create an appointment object. */
public class Appointments {
    private int Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private String Type;
    private Timestamp Start;
    private Timestamp End;
    private int Customer_ID;
    private int User_ID;
    private int Contact_ID;



    private String Contact_Name;
    private LocalDateTime localStart;
    private LocalDateTime localEnd;
    Contacts apptContact;
    public static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
    private static String dbContact;

    /** The Appointments constructor method.
     * @param appointment_ID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customer_ID
     * @param user_ID
     * @param contact_ID
     * @param contactName
     * @param appointmentContact*/
    public Appointments(int appointment_ID, String title, String description, String location, String type,
                        Timestamp start, Timestamp end, int customer_ID, int user_ID, int contact_ID, String contactName,
                        Contacts appointmentContact) {
        this.Appointment_ID = appointment_ID;
        this.Title = title;
        this.Description = description;
        this.Location = location;
        this.Type = type;
        this.Start = start;
        this.End = end;
        this.Customer_ID = customer_ID;
        this.User_ID = user_ID;
        this.Contact_ID = contact_ID;
        this.Contact_Name = contactName;
        this.apptContact = appointmentContact;
    }



    /** Appointment Constructor. This overloaded constructor adds in the contactName.
     * @param appointment_ID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customer_ID
     * @param user_ID
     * @param contact_ID
     * @param contactName*/
    public Appointments(int appointment_ID, String title, String description, String location, String type,
                        Timestamp start, Timestamp end, int customer_ID, int user_ID, int contact_ID, String contactName) {
        this.Appointment_ID = appointment_ID;
        this.Title = title;
        this.Description = description;
        this.Location = location;
        this.Type = type;
        this.Start = start;
        this.End = end;
        this.Customer_ID = customer_ID;
        this.User_ID = user_ID;
        this.Contact_ID = contact_ID;
        this.Contact_Name = contactName;
    }

    /** Appointment Constructor. This overloaded constructor is used when the contactName and appointmentContact are not
     * needed.
     * @param appointment_ID int variable used to set the appointments ID.
     * @param title String variable used to set the appointments title.
     * @param description String variable used to set the appointment description.
     * @param location String variable used to set the location for the appointment.
     * @param type String variable used to set the type of appointment.
     * @param start Timestamp variable used to set the appointment's start time.
     * @param end Timestamp variable used to set the appointment's end time.
     * @param customer_ID int variable used to set the appointment's customer ID.
     * @param user_ID int variable used to set the user's ID of who it is assigned to.
     * @param contact_ID int variable used to set which contact ID will be assigned to the appointment.*/
    public Appointments(int appointment_ID, String title, String description, String location, String type,
                        Timestamp start, Timestamp end, int customer_ID, int user_ID, int contact_ID) {
        this.Appointment_ID = appointment_ID;
        this.Title = title;
        this.Description = description;
        this.Location = location;
        this.Type = type;
        this.Start = start;
        this.End = end;
        this.Customer_ID = customer_ID;
        this.User_ID = user_ID;
        this.Contact_ID = contact_ID;
    }

    /** Appointment Constructor. This overloaded constructor is used when a variety of different variables are being
     * saved to the appointment class. */
    public Appointments() {
    }

    /** The findContact() method. This method finds the contant name by using the contact ID that is provided to the method
     * as a parameter.
     * @param contact_ID integer variable used to find the associated contact by ID.*/
    public static String findContact(int contact_ID) {
        String selectContact = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        String contact = null;

        try {
            DBQuery.setPreparedStatement(conn, selectContact);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1, contact_ID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                contact = rs.getString("Contact_Name");
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return contact;
    }


    // ** you can use this to find out contact schedule. create observable list of all contacts and populate combobox.
    // ** when a user selects a contact select all appointments where contact_ID's match.
    /** The getDBAppointments() method. This method clears data out of the observable list allAppointments and then
     * uses a select statement to pull in all of the appointment data that is stored in the database. It will then create
     * appointment objects and add those objects back into the allAppointments list.*/
    public static void getDBAppointments() {
        allAppointments.clear();
        int dbAppointmentID;
        String dbTitle;
        String dbDescription;
        String dbLocation;
        String dbType;
        Timestamp dbStart;
        Timestamp dbEnd;
        int dbCustomerID;
        int dbUserID;
        int dbContactID;
        String dbContactName;

        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM appointments";

        try {
            statement.execute(selectStatement);
        } catch (SQLException error) {

        }


        try {
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                dbAppointmentID = rs.getInt("appointment_ID");
                dbTitle = rs.getString("title");
                dbDescription = rs.getString("description");
                dbLocation = rs.getString("location");
                dbType = rs.getString("type");
                dbStart = rs.getTimestamp("start");
                dbEnd = rs.getTimestamp("end");
                dbCustomerID = rs.getInt("customer_ID");
                dbUserID = rs.getInt("user_ID");
                dbContactID = rs.getInt("contact_ID");
                dbContactName = findContact(dbContactID);

                Appointments newAppointment = new Appointments(dbAppointmentID, dbTitle, dbDescription, dbLocation,
                        dbType, dbStart, dbEnd, dbCustomerID, dbUserID, dbContactID, dbContactName);
                newAppointment.setLocalStart(TimeConversion.timeStampToLocal(dbStart));
                newAppointment.setLocalEnd(TimeConversion.timeStampToLocal(dbEnd));
                allAppointments.add(newAppointment);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    // Getters
    /** @return the allAppointments list. */
    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }

    /** @return the Appointment_ID variable. */
    public int getAppointment_ID() {
        return Appointment_ID;
    }

    /** @return The title variable. */
    public String getTitle() {
        return Title;
    }

    /** @return The location variable */
    public String getLocation() {
        return Location;
    }

    /** @return The Type variable. */
    public String getType() {
        return Type;
    }

    /** @return The Start variable. */
    public Timestamp getStart() {
        return Start;
    }

    /** @return The End variable.*/
    public Timestamp getEnd() {
        return End;
    }

    /** @return The customer_ID variable */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /** @return The getUser_ID variable */
    public int getUser_ID() {
        return User_ID;
    }

    /** @return The getContact_ID variable. */
    public int getContact_ID() {
        return Contact_ID;
    }

    /** @return The apptContact variable. */
    public Contacts getContact() {
        return apptContact;
    }

    /** @return The localStart variable. */
    public LocalDateTime getLocalStart() {
        return localStart;
    }

    /** @return Description variable. */
    public String getDescription() {
        return Description;
    }

    /** @return The localEnd variable. */
    public LocalDateTime getLocalEnd() {
        return localEnd;
    }

    /** @return Contact_Name variable. Used in PropertyValueFactory. */
    public String getContact_Name() {
        return Contact_Name;
    }


    // Setters
    /** @param appointment_ID int type. */
    public void setAppointment_ID(int appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    /** @param title String type. */
    public void setTitle(String title) {
        Title = title;
    }

    /** @param description String type. */
    public void setDescription(String description) {
        Description = description;
    }

    /** @param location String type. */
    public void setLocation(String location) {
        Location = location;
    }

    /** @param type String type. */
    public void setType(String type) {
        Type = type;
    }

    /** @param start Timestamp type. */
    public void setStart(Timestamp start) {
        Start = start;
    }

    /** @param end Timestamp type. */
    public void setEnd(Timestamp end) {
        End = end;
    }

    /**
     * @param customer_ID int type.
     */
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /** @param user_ID int type. */
    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    /**
     * @param contact_ID int type
     */
    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }

    /**
     * @param db_Contact String type.
     */
    public static void setDbContact(String db_Contact) {
        dbContact = db_Contact;
    }

    /**
     * @param contact String type
     */
    public void setContact(String contact) {
        Contact_Name = contact;
    }

    /**
     * @param localStart LocalDateTime type.
     */
    public void setLocalStart(LocalDateTime localStart) {
        this.localStart = localStart;
    }

    /**
     * @param localEnd LocalDateTime type.
     */
    public void setLocalEnd(LocalDateTime localEnd) {
        this.localEnd = localEnd;
    }

    /**
     * @param contact_Name String type.
     */
    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }
}
