package Model;

import DBAccess.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;
import static Model.Appointments.allAppointments;
import static Model.Appointments.getAllAppointments;
import static DBAccess.DBConnect.conn;

/** The Customers class. This class is used to create a Customer object and will hold the data contained in the database
 * in the customer table. */
public class Customers {
    private static ObservableList<Appointments> associatedAppointments = FXCollections.observableArrayList();
    private static ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
    public static ObservableList<Integer> customerIDs = FXCollections.observableArrayList();

    private int Customer_ID;
    private String Customer_Name;
    private String Address;
    private String Postal_Code;
    private String Phone;
    private Date Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;
    private int Division_ID;
    private String Country;
    private String StateProv;
    private int Country_ID;

    /** The Customers() constructor. This constructor is used to set the variable for the Customer objects.
     * @param customer_ID Used to set the Customer_ID variable.
     * @param customer_Name Used to set the Customer_Name variable.
     * @param address Used to set the Address Variable.
     * @param postal_Code Used to set the Postal_Code variable.
     * @param phone Used to set the Phone variable.
     * @param divisionID Used to set the Division_ID variable.
     * @param country_ID Used to set the Country_ID variable.
     * @param division Used to set the division name attached to the Division_ID variable.
     * @param country Used to set the country name attached to the Country_ID variable.*/
    public Customers(int customer_ID, String customer_Name, String address, String postal_Code, String phone,
                     int divisionID, int country_ID, String division, String country) {
        this.Customer_ID = customer_ID;
        this.Customer_Name = customer_Name;
        this.Address = address;
        this.Postal_Code = postal_Code;
        this.Phone = phone;
        this.Division_ID = divisionID;
        this.Country_ID = country_ID;
        this.Country = country;
        this.StateProv = division;
    }

    /** The getDBCustomers() method. This method uses a SELECT statement to gather the data from the customers table in
     * the database. It will then take that data and create a Customer object and add it to the allCustomers observable
     * list. This list is cleared everytime that this method is ran to avoid multiple entries of the same object.*/
    public static void getDBCustomers() {
        allCustomers.clear();
        int dbCustomerID;
        String dbCustomerName;
        String dbAddress;
        String dbPostalCode;
        String dbPhone;
        int dbDivisionID;
        String dbCountry;
        int dbCountryID;
        String dbDivision;

        Statement statement = DBQuery.getStatement();

        String selectCountryDivision = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, " +
                "customers.Phone, customers.Division_ID, first_level_divisions.Division,countries.Country_ID, countries.Country FROM" +
                " customers, first_level_divisions, countries WHERE customers.Division_ID = first_level_divisions.Division_ID " +
                "AND first_level_divisions.Country_ID = countries.Country_ID;";

        try {
            statement.execute(selectCountryDivision);
        } catch (SQLException error) {
            error.printStackTrace();
        }

        try {
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                dbCustomerID= rs.getInt("Customer_ID");
                dbCustomerName = rs.getString("Customer_Name");
                dbAddress = rs.getString("Address");
                dbPostalCode = rs.getString("Postal_Code");
                dbPhone = rs.getString("Phone");
                dbDivisionID = rs.getInt("Division_ID");
                dbDivision = rs.getString("Division");
                dbCountryID = rs.getInt("Country_ID");
                dbCountry = rs.getString("Country");

                Customers newCustomer = new Customers(dbCustomerID, dbCustomerName, dbAddress, dbPostalCode, dbPhone,
                        dbDivisionID,dbCountryID, dbCountry, dbDivision);
                if (allCustomers.contains(newCustomer) == false) {
                    addCustomer(newCustomer);
                }
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /** The getCustomerIDs() method. This method is used to gather all the Customer_ID's from the customers table in the
     * database. The IDs are then added to an observable list which is used to populate a combo box in the appointments
     * screen. */
    public static void getCustomerIDs() {
        customerIDs.clear();
        int dbCustomerID;


        String selectStatment = "SELECT Customer_ID FROM customers";
        Statement statement = DBQuery.getStatement();

        try {
            statement.execute(selectStatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                dbCustomerID = rs.getInt("Customer_ID");
                customerIDs.add(dbCustomerID);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /** @return associatedAppointments This getter returns the associatedAppointments observable list. */
    public static ObservableList<Appointments> getAssociatedAppointments() {
        return associatedAppointments;
    }

    /** The addCustomer() method. This method will add a new Customer object to the allCustomers observable list.
     * @param newCustomer The customer object being added.  */
    public static void addCustomer(Customers newCustomer) {
        allCustomers.add(newCustomer);
    }

    /** The addAppointment() method. This method takes an Appointment object and adds it to the associated appointments
     * observable list.
     * @param newAppointment The appointment object being added to the associatedAppointments list.*/
    public static void addAppointment(Appointments newAppointment) {
        associatedAppointments.add(newAppointment);
    }

    // Returns the observable list of customers
    /** @return allCustomers This will return the allCustomers list.*/
    public static ObservableList<Customers> getAllCustomers() {return allCustomers;}


    // Getters
    /** @return Customer_ID This will return the Customer_ID variable. */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /** @return Customer_Name This will return the Customer_Name variable */
    public String getCustomer_Name() {
        return Customer_Name;
    }

    /** @return Address This returns the Address variable */
    public String getAddress() {
        return Address;
    }

    /** @return Postal_Code This will return the Postal_Code variable.*/
    public String getPostal_Code() {
        return Postal_Code;
    }

    /** @return Phone This will return the Phone variable. */
    public String getPhone() {
        return Phone;
    }

    /** @return Create_Date This will return the Create_Date variable. */
    public Date getCreate_Date() {
        return Create_Date;
    }

    /** @return Created_By This will return the Created_By variable. */
    public String getCreated_By() {
        return Created_By;
    }

    /** @return Last_Update This will return the Last_Update variable. */
    public Timestamp getLast_Update() {
        return Last_Update;
    }

    /** @return Last_Updted_By This will return the Last_Updated_By variable. */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    /** @return Division_ID This will return the Division_ID variable. */
    public int getDivision_ID() {
        return Division_ID;
    }

    /** @return Country_ID This will return the Country_ID variable. */
    public int getCountry_ID() {
        return Country_ID;
    }



    // Setters
    /** @param customer_ID an int is passed to set the ID for the customer.*/
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /** @param customer_Name a String is passed to set the Customer_Name. */
    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    /** @param address a String is passed to set the address for the customer. */
    public void setAddress(String address) {
        Address = address;
    }

    /** @param postal_Code a String is passed to set the postal code for the customers address. */
    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }

    /** @param phone a String is passed to set the phone number for the customer. */
    public void setPhone(String phone) {
        Phone = phone;
    }

    /** @param create_Date a Date is passed to set the Create_Date for the customer. */
    public void setCreate_Date(Date create_Date) {
        Create_Date = create_Date;
    }

    /** @param created_By a String is passed to set who created this customer. */
    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /** @param last_Update a Timestamp is passed to set when the last_Update occured for this customer. */
    public void setLast_Update(Timestamp last_Update) {
        Last_Update = last_Update;
    }

    /** @param last_Updated_By a String is passed to keep track of who last updated this customer in the system. */
    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }

    /** @param division_ID an int is passed to set the Division ID for the object. */
    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    /** @param country_ID an int is passed to set the ID for the country of the customer. Used as a foreign key constraint
     *  in the database.*/
    public void setCountry_ID(int country_ID) {
        Country_ID = country_ID;
    }

    /** @return This will return a String of the country name associated with the customer. */
    public String getCountry() {
        return Country;
    }

    /** @param country A string is used to set the name of the country associated with the Customer. */
    public void setCountry(String country) {
        Country = country;
    }

    /** @return This will return a String of the State or province listed for the customer. */
    public String getStateProv() {
        return StateProv;
    }

    /** @param stateProv Uses a String to set the state/province for the customer. */
    public void setStateProv(String stateProv) {
        StateProv = stateProv;
    }

}
