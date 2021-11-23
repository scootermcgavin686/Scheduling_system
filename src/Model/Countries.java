package Model;

import DBAccess.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import static DBAccess.DBConnect.conn;

/** The Countries class. This class creates a Country object and is used to keep track of what countries customers are
 * located. */
public class Countries {
    private int Country_ID;
    private String Country;
    private Date Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;
    public static ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    /** The Countries overloaded constructor. This constructor is used to create the country object and takes in two parameters.
     * @param dbCountry The dbCountry is a string type that is used to hold the country's name.
     * @param dbCountryID The dbCountryID is an int type that is used to hold the country's ID to identify the object in
     * the program and in the database.*/
    public Countries(int dbCountryID, String dbCountry) {
        this.Country_ID = dbCountryID;
        this.Country = dbCountry;
    }

    @Override
    /** The toString() method. This will return a string when an object is referenced. */
    public String toString() {
        return  Country;
    }

    /** The Countries constructor. This constructor is the main one used that holds all the information that is contained
     * in the database.
     * @param CountryID This int type is used to hold the ID variable for the country object.
     * @param country This String type is used to hold the countries name.
     * @param CreateDate This Date type variable is used to hold the information of when the information was created in
     * the database.
     * @param CreatedBy This String type variable is used to hold the user who created the information in the database.
     * @param LastUpdate This Timestamp variable is used to hold the information of when the last update to the data was
     * made.
     * @param LastUpdatedBy This String type variable is used to hold the data of who last updated the data.*/
    public Countries(int CountryID, String country, Date CreateDate, String CreatedBy, Timestamp LastUpdate,
                     String LastUpdatedBy){
        Country_ID = CountryID;
        Country = country;
        Create_Date = CreateDate;
        Created_By = CreatedBy;
        Last_Update = LastUpdate;
        Last_Updated_By = LastUpdatedBy;
    }

    /** @param country The country variable is a String type that is used to set the name of the country. */
    public Countries(String country){
        this.Country = country;
    }

    /** The getDBCountries() method. This method searches the database with the use of a SELECT statement to find all
     * the countries in the countries table. It will pull the data in and create a Country object and adds the object
     * to the allCountries list. The list is cleared each time that it is ran to avoid multiple entries of the same
     * objects.*/
    public static void getDBCountries() {
        allCountries.clear();
        try {
            Statement statement = DBQuery.getStatement();
            String selectStatement = "SELECT * FROM countries";
            statement.execute(selectStatement);

            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                int dbCountryID = rs.getInt("country_ID");
                String dbCountry = rs.getString("country");
                Countries newCountry = new Countries(dbCountryID, dbCountry);
                allCountries.add(newCountry);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }



    // Getters
    /** @return allCountries Returns the allCountries Observable list. */
    public static ObservableList<Countries> getAllCountries() {
        return allCountries;
    }

    /** @return Country_ID Returns the Country_ID int variable attached to the country object. */
    public int getCountry_ID() {
        return Country_ID;
    }

    /** @return Country Returns the Country String variable  */
    public String getCountry() {
        return Country;
    }

    /** @return Create_Date Returns the Create_Date Timestamp variable.*/
    public Date getCreate_Date() {
        return Create_Date;
    }

    /** @return Created_By Returns the Created_By String type variable. */
    public String getCreated_By() {
        return Created_By;
    }

    /** @return Last_Update Returns the Last_Update Timestamp variable. */
    public Timestamp getLast_Update() {
        return Last_Update;
    }

    /** @return Last_Updated_By Returns the Last_Updated_By String variable. */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    // Setters
    /** @param country_ID The country_ID int variable is used to set the value of the country_ID for the country object. */
    public void setCountry_ID(int country_ID) {
        Country_ID = country_ID;
    }

    /** @param country The country String variable is used to set the country variable for the country object. */
    public void setCountry(String country) {
        Country = country;
    }

    /** @param create_Date The create_Date Timestamp variable is used to set the Create_Date for the country object.*/
    public void setCreate_Date(Date create_Date) {
        Create_Date = create_Date;
    }

    /** @param created_By The created_By variable is the variable passed to set the Created_By variable for the country
     *  object */
    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /** @param last_Update The last_Update variable is passed to set the Last_Update variable in the country object. */
    public void setLast_Update(Timestamp last_Update) {
        Last_Update = last_Update;
    }

    /** @param last_Updated_By This variable is passed to set the Last_Update_By String variable in the country object. */
    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }
}
