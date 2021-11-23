package Controllers;

import DBAccess.DBQuery;
import DBAccess.TimeConversion;
import Model.Appointments;
import Model.Contacts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import static Controllers.AppointmentController.customerAppt;
import static Controllers.AppointmentController.getDBCustomerAppointments;
import static Controllers.CustomerController.selectedAppointment;
import static DBAccess.DBConnect.conn;
import static DBAccess.TimeConversion.*;
import static Model.Contacts.getDBContacts;
import static Model.Customers.customerIDs;
import static Model.Customers.getCustomerIDs;
import static Model.Users.getUserIDs;
import static Model.Users.userIDs;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

/** The EditAppointmentController class. This class will implements the Initializable interface and is responsible for
 * all actions that happen on the edit appointment screen. */
public class EditAppointmentController implements Initializable {
    Alert alert = new Alert(Alert.AlertType.NONE);

    @FXML
    TextField appointmentText;
    @FXML
    TextField titleText;
    @FXML
    TextField descriptionText;
    @FXML
    TextField locationText;
    @FXML
    TextField typeText;
    @FXML
    TextField startDateText;
    @FXML
    TextField startTimeText;
    @FXML
    TextField endDateText;
    @FXML
    TextField endTimeText;
    @FXML
    TextField customerIDText;
    @FXML
    TextField userIDText;

    public ComboBox<Contacts> contactsComboBox;
    public ComboBox<Integer> customerIDCombo;
    public ComboBox<Integer> userIDCombo;

    /** The back() ActionEvent. This event will be triggered when the user clicks on the back button in the edit appointment
     * screen. This will display an alert to the user letting them know any changes will not be saved and direct them
     * back to the customer screen.*/
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

    /** The saveAppointment() ActionEvent. This event is triggered when the user clicks on the save button in the edit
     * appointment screen. When selected it will check to see if all the given data is valid through the validateAppointment()
     * method, and then it will run the update statement to change the given appointment in the database.*/
    public void saveAppointment(ActionEvent actionEvent) throws Exception {
        try {
            if (validateAppointment()) {
                String title = titleText.getText();
                String description = descriptionText.getText();
                String location = locationText.getText();
                String type = typeText.getText();

                LocalTime start_Time = stringToLocalTime(startTimeText.getText());
                LocalDate start_Date = stringToLocalDate(startDateText.getText());

                LocalTime end_Time = stringToLocalTime(endTimeText.getText());
                LocalDate end_Date = stringToLocalDate(startDateText.getText());

                Timestamp startTimestamp = (localToTimeStamp(TimeConversion.localDateLocalTimeToLDT(start_Date, start_Time)));
                Timestamp endTimestamp = localToTimeStamp(TimeConversion.localDateLocalTimeToLDT(end_Date, end_Time));

                int customerID = customerIDCombo.getSelectionModel().getSelectedItem();
                int userID = userIDCombo.getSelectionModel().getSelectedItem();
                Contacts selectedContact = contactsComboBox.getSelectionModel().getSelectedItem();
                int contactID = selectedContact.getContact_ID();
                String customerName = selectedContact.getContact_Name();
                String updateStatement = "UPDATE appointments SET Title = ?, Description = ?," +
                        " Location = ?, Type = ?, Start = ?, " +
                        "End = ?, Customer_ID = ?, User_ID = ?, " +
                        "Contact_ID = ? WHERE Appointment_ID = ?";

                DBQuery.setPreparedStatement(conn, updateStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();

                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, startTimestamp);
                ps.setTimestamp(6, endTimestamp);
                ps.setInt(7, customerID);
                ps.setInt(8, userID);
                ps.setInt(9, contactID);
                ps.setInt(10, selectedAppointment.getAppointment_ID());
                ps.execute();

                Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setTitle("Scheduling System");
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /** The validateAppointment() method. This method is responsible for checking that there is data in each text field
     * and that it is valid to use. The getDBCustomerAppointments method will run to pull in all appointments that
     * contain the same customerID that has been selected and then verify that there will be no overlapping appointments.
     * This method will also check to see if the given times fall within the given business hours of 8:00am EST to 10:00pm
     * EST.*/
    public boolean validateAppointment() {
        StringBuilder errors = new StringBuilder();
        int customerID = customerIDCombo.getSelectionModel().getSelectedItem();

        getDBCustomerAppointments(customerID);

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
            if (startTimeText.getText().trim().isEmpty()) {
                errors.append("- Enter a start time for the appointment.\n");
            }
            if (startDateText.getText().trim().isEmpty()) {
                errors.append("- Enter a start date for the appointment.\n");
            }
            if (endTimeText.getText().trim().isEmpty()) {
                errors.append("- Enter an ending time for the appointment.\n");
            }
            if (customerIDCombo.getSelectionModel().isEmpty()) {
                errors.append("- Enter a customer ID for the appointment.\n");
            }
            if (userIDCombo.getSelectionModel().isEmpty()) {
                errors.append("- Enter a user ID for the appointment.\n");
            }
            if (contactsComboBox.getSelectionModel().isEmpty()) {
                errors.append("- Please choose a contact to assign the appointment to.\n");
            }

            if (startTimeText.getText().isEmpty() || startDateText.getText().isEmpty() || endTimeText.getText().isEmpty() ||
                    !isTime(startTimeText.getText()) || !isTime(endTimeText.getText()) || !isDate(startDateText.getText())){
                errors.append("- Please enter valid time and date information.\n");
            } else {
                LocalDateTime proposedStart = TimeConversion.localDateLocalTimeToLDT(stringToLocalDate(startDateText.getText())
                        , stringToLocalTime(startTimeText.getText()));

                LocalDateTime proposedEnd = TimeConversion.localDateLocalTimeToLDT(stringToLocalDate(startDateText.getText())
                        , stringToLocalTime(endTimeText.getText()));

                ZonedDateTime localStart = proposedStart.atZone(ZoneId.systemDefault());
                ZonedDateTime estStartZDT = localStart.withZoneSameInstant(ZoneId.of("America/New_York"));
                LocalTime estStart = estStartZDT.toLocalTime();

                ZonedDateTime localEnd = proposedEnd.atZone(ZoneId.systemDefault());
                ZonedDateTime estEndZDT = localEnd.withZoneSameInstant(ZoneId.of("America/New_York"));
                LocalTime estEnd = estEndZDT.toLocalTime();

                int aptID = Integer.parseInt(appointmentText.getText());
                for (Appointments a: customerAppt) {
                    boolean isOverlap = false;
                    if (a.getAppointment_ID() != aptID) {
                        if ((a.getLocalStart().isAfter(proposedStart) || a.getLocalStart().isEqual(proposedStart)) &&
                                a.getLocalStart().isBefore(proposedEnd)) {
                            isOverlap = true;
                        }
                        if ((a.getLocalEnd().isAfter(proposedStart) || a.getLocalEnd().isEqual(proposedStart)) &&
                                (a.getLocalEnd().isBefore(proposedEnd) || a.getLocalEnd().isEqual(proposedEnd))) {
                            isOverlap = true;
                        }
                        if ((a.getLocalStart().isBefore(proposedStart) || a.getLocalStart().isEqual(proposedStart)) &&
                                (a.getLocalEnd().isAfter(proposedEnd) || a.getLocalEnd().isEqual(proposedEnd))) {
                            isOverlap = true;
                        }
                        if (isOverlap) {
                            errors.append("- Appointments already scheduled for the Customer at this time.");
                            break;
                        }
                    }
                }
                if (estStart.isBefore(LocalTime.of(8,0)) || estStart.isAfter(LocalTime.of(22,0))) {
                    errors.append("Start time is outside of business hours.\n");
                }
                if (estEnd.isBefore(LocalTime.of(8, 0)) || estEnd.isAfter(LocalTime.of(22, 0))) {
                    errors.append("The ending time is outside business hours.\n");
                }
                if (localTimeDateToTime(proposedStart).isAfter(localTimeDateToTime(proposedEnd))) {
                    errors.append("End time must be after start time.\n");
                }
            }
        } catch (NumberFormatException e) {
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

    /** The isTime() method. This method will verify that the given time is in the correct format and if it is not will
     * return false. */
    public boolean isTime(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (DateTimeException | NullPointerException e) {
            return false;
        }
    }

    /** The isDate() method. This method is used to verify that a valid date as been entered into the start date text
     * field. If not it will return false. */
    public boolean isDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeException | NullPointerException e) {
            return false;
        }
    }

    /** The initialize() method. This method is part of the Initializable interface and will run the getDBContacts(),
     * getUserIDs(), and getCustomerIDs() methods and load the respective data into the appropriate combobox's.*/
    public void initialize(URL location, ResourceBundle resources) {
        getDBContacts();
        getUserIDs();
        getCustomerIDs();

        Appointments selectedAppointment = CustomerController.selectedAppointment;
        LocalDate startDate = localTimeDateToDate(timeStampToLocal(selectedAppointment.getStart()));
        LocalTime startTime = localTimeDateToTime(timeStampToLocal(selectedAppointment.getStart()));
        LocalTime endTime = localTimeDateToTime(timeStampToLocal(selectedAppointment.getEnd()));
        appointmentText.setText(String.valueOf(selectedAppointment.getAppointment_ID()));
        titleText.setText(selectedAppointment.getTitle());
        descriptionText.setText(selectedAppointment.getTitle());
        locationText.setText(selectedAppointment.getLocation());
        typeText.setText(selectedAppointment.getType());
        startDateText.setText(startDate.toString());
        startTimeText.setText(String.valueOf(startTime));
        endTimeText.setText(String.valueOf(endTime));

        customerIDCombo.setItems(customerIDs);
        for(Integer id: customerIDCombo.getItems()) {
            if(selectedAppointment.getCustomer_ID() == id) {
                customerIDCombo.setValue(id);
            }
        }

        userIDCombo.setItems(userIDs);
        for(Integer id: userIDCombo.getItems()) {
            if(selectedAppointment.getUser_ID() == id) {
                userIDCombo.setValue(id);
            }
        }

        contactsComboBox.setItems(Contacts.allContacts);
        for(Contacts contact: contactsComboBox.getItems()){
            if(selectedAppointment.getContact_ID() == contact.getContact_ID()) {
                contactsComboBox.setValue(contact);
            }
        }
    }
}
