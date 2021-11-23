package Model;

import DBAccess.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;

import static DBAccess.DBConnect.conn;
import static DBAccess.TimeConversion.*;
import static Model.Appointments.allAppointments;
import static Model.Appointments.getDBAppointments;

/** The login class. This class will use a select statement to gather all the user data from the database. It will collect
 * the User_ID, User_Name, and Password for each row in the database table. This class is responsible for handling logging
 * in to the application and the actions associated with that.*/
public class Login {
    static Alert alert = new Alert(Alert.AlertType.NONE);
    static ObservableList<Appointments> upcomingAppointments = FXCollections.observableArrayList();

    /** The ULogin() method. This method uses the select statement to connect to the database and pull in the data to the
     * application. It will then check to see if there are any users matching the text that the user has given the application.
     * If a user is found, it will check to see if the password matches. When the user enters the correct information an
     * alert will display to let the user know of any upcoming appointments.
     **/
    public static boolean ULogin(String user_Name, String password) {
        Statement statement = DBQuery.getStatement();
        String insertStatement = "SELECT User_ID, User_Name, Password FROM users";

        try {
            statement.execute(insertStatement);
        } catch (SQLException error) {

        }

        try {
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                int User_ID = rs.getInt("User_ID");
                String User_Name = rs.getString("User_Name");
                String Password = rs.getString("Password");

                appointments(User_ID);
                if (user_Name.equals(User_Name)) {
                    if (Password.equals(password)) {
                        if (!upcomingAppointments.isEmpty()) {
                            StringBuilder futureAppointments = new StringBuilder();
                            alert.setAlertType(Alert.AlertType.INFORMATION);
                            alert.setTitle("Upcoming Appointments");
                            for (Appointments app: upcomingAppointments) {
                                futureAppointments.append("Appointment ID: " + app.getAppointment_ID() +
                                        " is beginning at " + localTimeDateToTime(app.getLocalStart()) +
                                        " on " + localTimeDateToDate(app.getLocalStart())+ "\n");
                            }
                            alert.setContentText(futureAppointments.toString());
                            alert.showAndWait();
                            return true;
                        } else {
                            alert.setAlertType(Alert.AlertType.INFORMATION);
                            alert.setTitle("Upcoming Appointments");
                            alert.setContentText("There are no upcoming appointments");
                            alert.showAndWait();
                            return true;
                        }
                    }
                }

            }
        } catch (SQLException error) {

        }
        return false;
    }

    /**Lambda expression. The implemented lambda expression is an anonymous lambda that will return true if the
     * appointment in allAppointments matches the userID supplied to the method. The appointments() method is used
     * to filter the allAppointments list into a list of appointments for the given userID.
     * @param userID The integer passed into the method to check if there are any appointments for the associated ID. */
    public static void appointments(int userID) {
        getDBAppointments();
        ObservableList<Appointments> usersAppointments = allAppointments.filtered(a -> {
            if(a.getUser_ID() == userID)
                return true;
            return false;
        });

        for (Appointments a: usersAppointments) {
            LocalDateTime start = timeStampToLocal(a.getStart());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime plus15 = LocalDateTime.now().plusMinutes(15);
            if (start.isAfter(now) && (start.isBefore(plus15) || start.isEqual(plus15))) {
                upcomingAppointments.add(a);
            }
        }
    }
}
