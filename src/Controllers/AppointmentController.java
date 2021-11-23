package Controllers;


import DBAccess.DBQuery;
import DBAccess.TimeConversion;
import Model.Appointments;
import Model.Contacts;
import Model.VerifyTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import static DBAccess.DBConnect.conn;
import static DBAccess.TimeConversion.*;
import static Model.Contacts.getDBContacts;
import static Model.Customers.customerIDs;
import static Model.Customers.getCustomerIDs;
import static Model.Users.getUserIDs;
import static Model.Users.userIDs;

/** The AppointmentController class. This class implements the Initializable inferface. It is responsible for handling
 * all of the actions on the add appointment screen. */
public class AppointmentController implements Initializable {
    Alert alert = new Alert(Alert.AlertType.NONE);

    public ComboBox<Contacts> contactsComboBox;
    public ComboBox<Integer> customerIDCombo;
    public ComboBox<Integer> userIDCombo;
    public static ObservableList<Appointments> customerAppt = FXCollections.observableArrayList();
    int dbmaxID;

    @FXML
    TextField appointmentIDText;
    @FXML
    TextField titleText;
    @FXML
    TextField descriptionText;
    @FXML
    TextField locationText;
    @FXML
    TextField typeText;
    @FXML
    TextField startTime;
    @FXML
    TextField startDate;
    @FXML
    TextField endTime;


    /** The back(). This ActionEvent is triggered when the user clicks on the back button on the add appointment screen.
     * An alert will be displayed that lets the user know that any work will not be saved. */
    public void back(ActionEvent actionEvent) throws Exception {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("This appointment will not be saved.\nDo you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try{
                Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setTitle("Main");
                stage.setScene(scene);
                stage.show();
            } catch (NullPointerException error) {
                error.printStackTrace();
            }
        }

    }


    /** The saveAppointment() method. This method inserts a new appointment into the database. If the validateAppointment
     * returns true it will collect all of the Data and then run an insert statement to put the appointment into the
     * database.*/
    public void saveAppointment(ActionEvent actionEvent) throws Exception {
        try {
            if (validateAppointment()) {
                String title = titleText.getText();
                String description = descriptionText.getText();
                String location = locationText.getText();
                String type = typeText.getText();

                LocalTime start_Time = stringToLocalTime(startTime.getText());
                LocalDate start_Date = stringToLocalDate(startDate.getText());

                LocalTime end_Time = stringToLocalTime(endTime.getText());
                LocalDate end_Date = stringToLocalDate(startDate.getText());

                Timestamp startTimestamp = (localToTimeStamp(TimeConversion.localDateLocalTimeToLDT(start_Date, start_Time)));
                Timestamp endTimestamp = localToTimeStamp(TimeConversion.localDateLocalTimeToLDT(end_Date, end_Time));

                int customerID = customerIDCombo.getSelectionModel().getSelectedItem();
                int userID = userIDCombo.getSelectionModel().getSelectedItem();
                Contacts selectedContact = contactsComboBox.getSelectionModel().getSelectedItem();
                int contactID = selectedContact.getContact_ID();
                String customerName = selectedContact.getContact_Name();
                String insertStatement = "INSERT INTO appointments (Title, Description, Location, Type, Start," +
                        " End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                DBQuery.setPreparedStatement(conn, insertStatement);
                PreparedStatement preparedStatement = DBQuery.getPreparedStatement();

                preparedStatement.setString(1, title);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, location);
                preparedStatement.setString(4, type);
                preparedStatement.setTimestamp(5, startTimestamp);
                preparedStatement.setTimestamp(6, endTimestamp);
                preparedStatement.setInt(7, customerID);
                preparedStatement.setInt(8, userID);
                preparedStatement.setInt(9, contactID);
                preparedStatement.execute();

                Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setTitle("Main");
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /** The validateAppointment method. This method will check to see if the text fields are empty and if they contain
     * valid data for their respect fields. This will also see if there is valid times and dates in those fields
     * and will check to see if they are within business hours EST and if they overlap with any appointments with the
     * customer. If it detects an error in any one of these fields it will add a message to a StringBuilder and will
     * give an alert with all the things that will need changed.
     * My lambda expression time. The implemented interface has a return statement of either true or false depending
     * on whether the string that is passed to it is a valid LocalTime.*/
    public boolean validateAppointment() {
        StringBuilder errors = new StringBuilder();

        if (!customerIDCombo.getSelectionModel().isEmpty()) {
            int customerID = customerIDCombo.getSelectionModel().getSelectedItem();
            getDBCustomerAppointments(customerID);
        } else {
            errors.append("- You must select a customer to assign the appointment to.\n");
        }

        try {
            if (titleText.getText().trim().isEmpty()) {
                errors.append("- Enter an appointment title.\n");
            }
            if (descriptionText.getText().trim().isEmpty()) {
                errors.append("- Enter an appointment description.\n");
            }
            if (locationText.getText().trim().isEmpty()) {
                errors.append("- Enter the appointments location.\n");
            }
            if (typeText.getText().trim().isEmpty()) {
                errors.append("- Enter the appointment type.\n");
            }
            // Eventually include methods to check business hours, weekends, overlapping appointment
            if (startTime.getText().trim().isEmpty()) {
                errors.append("- Enter a start time for appointment.\n");
            }
            if (startDate.getText().trim().isEmpty()) {
                errors.append("- Enter a start date for the appointment.\n");
                // return false;
            }
            if (endTime.getText().trim().isEmpty()) {
                errors.append("- Enter an ending time for the appointment.\n");
                // return false;
            }
            if (customerIDCombo.getSelectionModel().isEmpty()) {
                errors.append("- Please select a customer ID for the appointment\n");
            }
            if (userIDCombo.getSelectionModel().isEmpty()) {
                errors.append("- Please select a user ID for the appointment\n");
            }
            if (contactsComboBox.getSelectionModel().isEmpty()) {
                errors.append("- Please choose a contact to assign the appointment to.\n");
            }
            if (startTime.getText().isEmpty() || startDate.getText().isEmpty() || endTime.getText().isEmpty() ||
                    !time.isTime(startTime.getText()) || !time.isTime(endTime.getText()) || !isDate(startDate.getText())){
                errors.append("- Please enter valid time and date information.\n");
            } else {
                LocalDateTime proposedStart = TimeConversion.localDateLocalTimeToLDT(stringToLocalDate(startDate.getText())
                        , stringToLocalTime(startTime.getText()));

                LocalDateTime proposedEnd = TimeConversion.localDateLocalTimeToLDT(stringToLocalDate(startDate.getText())
                        , stringToLocalTime(endTime.getText()));

                ZonedDateTime localStart = proposedStart.atZone(ZoneId.systemDefault());
                ZonedDateTime estStartZDT = localStart.withZoneSameInstant(ZoneId.of("America/New_York"));
                LocalTime estStart = estStartZDT.toLocalTime();

                ZonedDateTime localEnd = proposedEnd.atZone(ZoneId.systemDefault());
                ZonedDateTime estEndZDT = localEnd.withZoneSameInstant(ZoneId.of("America/New_York"));
                LocalTime estEnd = estEndZDT.toLocalTime();

                for (Appointments a: customerAppt) {
                    boolean isOverlap = false;
                    // Removed if statement to see if it works
                        if ((a.getLocalStart().isAfter(proposedStart) || a.getLocalStart().isEqual(proposedStart)) &&
                                a.getLocalStart().isBefore(proposedEnd)) {
                            isOverlap = true;
                        }
                        if ((a.getLocalEnd().isAfter(proposedStart)) && (a.getLocalEnd().isBefore(proposedEnd) ||
                                a.getLocalEnd().isEqual(proposedEnd))) {
                            isOverlap = true;
                        }
//                        if ((a.getLocalEnd().isAfter(proposedStart) || a.getLocalEnd().isEqual(proposedStart)) &&
//                                (a.getLocalEnd().isBefore(proposedEnd) || a.getLocalEnd().isEqual(proposedEnd))) {
//                            isOverlap = true;
//                        }
                        if ((a.getLocalStart().isBefore(proposedStart) || a.getLocalStart().isEqual(proposedStart)) &&
                                (a.getLocalEnd().isAfter(proposedEnd) || a.getLocalEnd().isEqual(proposedEnd))) {
                            isOverlap = true;
                        }
                        if (isOverlap) {
                            errors.append("- Appointments already scheduled for the customer at this time.");
                            break;
                        }
                }
                if (estStart.isBefore(LocalTime.of(8, 0)) || estStart.isAfter(LocalTime.of(22, 0))) {
                    errors.append("- Start time is outside of business hours.\n");
                }
                if (estEnd.isBefore(LocalTime.of(8, 0)) || estEnd.isAfter(LocalTime.of(22, 0))) {
                    errors.append("- The ending time is outside business hours.\n");
                }
                if (localTimeDateToTime(proposedStart).isAfter(localTimeDateToTime(proposedEnd))) {
                    errors.append("- End time must be after start time.\n");
                }
            }
        } catch (DateTimeException | NumberFormatException e) {
            e.printStackTrace();
        }
        if (errors.length() > 0) {
            alert.setTitle("Error");
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /** The getDBCustomerAppointments() method. This method is responsible for making a call to the database and creating
     * a list of appointments with the customer chosen on the add appointment screen.*/
    public static void getDBCustomerAppointments(int CustomerID) {
        int dbAppointmentID;
        Timestamp dbStart;
        Timestamp dbEnd;

        String selectStatement = "SELECT * FROM appointments WHERE Customer_ID = ?";

        try {
            DBQuery.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.setInt(1, CustomerID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dbAppointmentID = rs.getInt("appointment_ID");
                dbStart = rs.getTimestamp("start");
                dbEnd = rs.getTimestamp("end");

                Appointments newA = new Appointments();
                newA.setLocalStart(TimeConversion.timeStampToLocal(dbStart));
                newA.setLocalEnd(TimeConversion.timeStampToLocal(dbEnd));
                newA.setAppointment_ID(dbAppointmentID);
                customerAppt.add(newA);
            }
        } catch (SQLException error) {
                error.printStackTrace();
            }
    }

    /** The getMaxID() function. The method that will assign an ID to the appointment so that when the validateAppointment()
     * runs it will not check itself during the overlap for loop. */
    public int getMaxID() {
        String selectID = "SELECT MAX(Appointment_ID) AS dbmaxID FROM appointments";
        try {
            DBQuery.setPreparedStatement(conn, selectID);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dbmaxID = rs.getInt("dbmaxID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbmaxID;
    }

    /** My lambda expression time. The implemented interface has a return statement of either true or false depending
     * on whether the string that is passed to it is a valid LocalTime.
     * t is the string that is passed to */
    public VerifyTime time = t -> {try {LocalTime.parse(t);return true;} catch (DateTimeException | NullPointerException e) {
        return false;}};

    /** The isDate() method. The isDate method accepts a string variable and tries to parse it as a LocalDate variable. If
     * it doesn't throw an error it returns as true and if it does it will return as false. */
    public boolean isDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeException | NullPointerException e) {
            return false;
        }
    }

    /** The initialize() method. This method is responsible for running database calls and loading the information into
     * combo boxes.*/
    public void initialize(URL location, ResourceBundle resources) {
        getDBContacts();
        getCustomerIDs();
        getUserIDs();
        contactsComboBox.setItems(Contacts.allContacts);
        customerIDCombo.setItems(customerIDs);
        userIDCombo.setItems(userIDs);
    }
}
