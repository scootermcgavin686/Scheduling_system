package Model;

import DBAccess.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

/** The Users class. This class is used to create a user object. The user is an object that is used to keep track of who
 * is using the application and has login credentials stored in the User_Name and Password variables. */
public class Users {
    private int User_ID;
    private String User_Name;
    private String Password;
    private Date Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Update_By;
    public static ObservableList<Integer> userIDs = FXCollections.observableArrayList();

    /** The Users() Constructor. This method takes parameters and assigns them to their respective variables in the user
     * object.
     * @param user_ID An int variable is passed to create the user ID
     * @param user_Name A string variable is passed to set the name of the user
     * @param password A string variable is passed to set the password associated with the user.
     * @param create_Date A Timestamp is passed to set the date it was created.
     * @param created_By A string is passed to set who created the user.
     * @param last_Update A Timestamp is passed to set the date of the last update of the user.
     * @param last_Update_By A String is passed to set who last updated the user.*/
    public Users(int user_ID, String user_Name, String password, Date create_Date, String created_By,
                 Timestamp last_Update, String last_Update_By) {

        User_ID = user_ID;
        User_Name = user_Name;
        Password = password;
        Create_Date = create_Date;
        Created_By = created_By;
        Last_Update = last_Update;
        Last_Update_By = last_Update_By;
    }

    public Users(String user_Name, String password) {
        User_Name = user_Name;
        Password = password;
    }

    /** The getUserIDs() method. This method uses a select statement to gather al user IDs that are stored in the database.
     * It will use a select statement to gather the data and then with the result set it will add them to the userIDs
     * observable list.*/
    public static void getUserIDs() {
        userIDs.clear();
        int dbUserID;

        String selectStatment = "SELECT User_ID FROM users";
        Statement statement = DBQuery.getStatement();

        try {
            statement.execute(selectStatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                dbUserID = rs.getInt("User_ID");
                userIDs.add(dbUserID);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
    // Getters
    public int getUser_ID() {
        return User_ID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public String getPassword() {
        return Password;
    }

    public Date getCreate_Date() {
        return Create_Date;
    }

    public String getCreated_By() {
        return Created_By;
    }

    public Timestamp getLast_Update() {
        return Last_Update;
    }

    public String getLast_Update_By() {
        return Last_Update_By;
    }

    // Setters
    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setCreate_Date(Date create_Date) {
        Create_Date = create_Date;
    }

    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    public void setLast_Update(Timestamp last_Update) {
        Last_Update = last_Update;
    }

    public void setLast_Update_By(String last_Update_By) {
        Last_Update_By = last_Update_By;
    }
}
