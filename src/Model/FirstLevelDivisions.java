package Model;

import DBAccess.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

/** The FirstLevelDivisions class. This class is responsible for creating FirstLevelDivision objects that will be populated
 * with data from the first_level_divisions table in the database.  */
public class FirstLevelDivisions {
    private int Division_ID;
    private String Division;
    private Date Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;
    private int Country_ID;

    public static ObservableList<FirstLevelDivisions> statesProv = FXCollections.observableArrayList();

    /** The FistLevelDivisions Constructor. This method builds a FirstLevelDivision object by accepting parameters and
     * assigning it to variable of the FirstLevelDivision class.*/
    public FirstLevelDivisions(int division_ID, String division, Date create_Date, String created_By,
                               Timestamp last_Update, String last_Updated_By, int country_ID) {
        Division_ID = division_ID;
        Division = division;
        Create_Date = create_Date;
        Created_By = created_By;
        Last_Update = last_Update;
        Last_Updated_By = last_Updated_By;
        Country_ID = country_ID;
    }

    @Override
    /** The toString() method. When a FirstLevelDivision object is referenced a String of the Division name will be returned
     * @return Division. */
    public String toString() {
        return  Division;
    }

    /** A FirstLevelDivisions Constructor. This overloaded constructor will build an object of this class and assign it
     * just the Division_ID, Division, and Country_ID fields. */
    public FirstLevelDivisions(int division_ID, String division, int country_ID) {
        this.Division_ID = division_ID;
        this.Division = division;
        this.Country_ID = country_ID;
    }

    /** The getDBFirstLevel() method. This method runs a select statement and retrieves the data in the first_level_divisions
     * table. Once the date is collected an object of this class will be built and added to an observable list, statesProv.*/
    public static void getDBFirstLevel(int country_ID) {
        statesProv.clear();
        try {
            Statement statement = DBQuery.getStatement();
            String selectStatement = "SELECT division_ID, division, country_ID FROM first_level_divisions WHERE country_ID = " + country_ID;
            statement.execute(selectStatement);

            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                int dbDivisionID = rs.getInt("division_ID");
                String dbDivision = rs.getString("division");
                int dbCountryID = rs.getInt("country_ID");
                FirstLevelDivisions firstLevel = new FirstLevelDivisions(dbDivisionID, dbDivision, dbCountryID);
                statesProv.add(firstLevel);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }


    // Getters
    /** @return This returns the Division_ID. */
    public int getDivision_ID() {
        return Division_ID;
    }

    /** @return This returns the Division. */
    public String getDivision() {
        return Division;
    }

    /** @return This returns the Create_date. */
    public Date getCreate_Date() {
        return Create_Date;
    }

    /** @return This returns the Created_By variable */
    public String getCreated_By() {
        return Created_By;
    }

    /** @return This will return a Timestamp that indicates when the last update occurred for the first_level_division
     * object */
    public Timestamp getLast_Update() {
        return Last_Update;
    }

    /** @return This returns a String indicating when the last time the object was updated. */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    /** @return This will return the Country Id associated with the first level division object. */
    public int getCountry_ID() {
        return Country_ID;
    }

    // Setters
    /** @param division_ID an int variable will set the ID for the Division object. */
    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    /** @param division a String will set the name of the division associated with the object. */
    public void setDivision(String division) {
        Division = division;
    }

    /** @param create_Date a Date will set when the object was created. */
    public void setCreate_Date(Date create_Date) {
        Create_Date = create_Date;
    }

    /** @param created_By this string will let people know what user created this object. */
    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /** @param last_Update The timestamp variable sets when the last time this object was updated. */
    public void setLast_Update(Timestamp last_Update) {
        Last_Update = last_Update;
    }

    /** @param last_Updated_By This string will set who last updated the first level division object. */
    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }

    /** @param country_ID This int variable will set the ID for the country associated with the first level division object.*/
    public void setCountry_ID(int country_ID) {
        Country_ID = country_ID;
    }
}
