package Controllers;

import DBAccess.DBQuery;
import Model.Appointments;
import Model.Customers;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.ResourceBundle;
import static DBAccess.DBConnect.conn;
import static Model.Appointments.allAppointments;
import static Model.Appointments.getDBAppointments;
import static Model.Customers.getDBCustomers;


/** The CustomerController Class. This class controls all the actions that are taken on the Customer screen. The
 * customer screen displays the customers that the company services as well as the appointments associated with those
 * customers.*/
public class CustomerController implements Initializable {
    Alert alert = new Alert(Alert.AlertType.NONE);

    private static Customers modifyCustomer;
    private static Appointments modifyAppointment;

    public TableView<Customers> customerTable;
    public TableColumn customerIDColumn;
    public TableColumn customerNameColumn;
    public TableColumn addressColumn;
    public TableColumn firstLevelColumn;
    public TableColumn postalCodeColumn;
    public TableColumn countryColumn;
    public TableColumn phoneColumn;
    public TableView<Appointments> appointmentsTable;
    public TableColumn<Appointments, Integer> appointmentIDColumn;
    public TableColumn<Appointments, String> titleColumn;
    public TableColumn<Appointments, String> descriptionColumn;
    public TableColumn<Appointments, String> locationColumn;
    public TableColumn<Appointments, String> contactColumn;
    public TableColumn<Appointments, String> typeColumn;
    public TableColumn<Appointments, String> startColumn;
    public TableColumn<Appointments, String> endColumn;
    public TableColumn<Appointments, Integer> customer_ID_Column;
    public TableColumn<Appointments, Integer> userIDColumn;

    public static Appointments selectedAppointment;
    public static Customers selectedCustomer;

    @FXML
    Button back;

    /** The showAppointments() MouseEvent. This event is triggered when a user selects a customer in the customer table.
     * It will create a select statement to select the appointments that are associated with the chosen customer. I have
     * included this function as my third report so that when a user deletes a customer they can see all the appointments
     * that will also be deleted. This method also makes it convenient to see all appointments with that customer in case
     * there is a scheduling conflict in the add or edit appointment screens.*/
    public void showAppointments(MouseEvent event) {
        appointmentsTable.getItems().clear();
        allAppointments.clear();
        modifyCustomer = customerTable.getSelectionModel().getSelectedItem();

        try {
            String selectStatement = "SELECT * FROM appointments WHERE customer_ID = ?";
            DBQuery.setPreparedStatement(conn, selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1, modifyCustomer.getCustomer_ID());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt("appointment_ID");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp apptStart = rs.getTimestamp("start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("end");
                LocalDateTime end = apptEnd.toLocalDateTime();
                int customer_ID = rs.getInt("customer_ID");
                int user_ID = rs.getInt("user_ID");
                int contact_ID = rs.getInt("contact_ID");
                String contactName = Appointments.findContact(contact_ID);

                // Creates appointment object and then assigns it with values using setters.
                Appointments appointment = new Appointments();
                appointment.setAppointment_ID(appointmentID);
                appointment.setTitle(title);
                appointment.setDescription(description);
                appointment.setLocation(location);
                appointment.setType(type);
                appointment.setStart(apptStart);
                appointment.setLocalStart(start);
                appointment.setEnd(apptEnd);
                appointment.setLocalEnd(end);
                appointment.setCustomer_ID(customer_ID);
                appointment.setUser_ID(user_ID);
                appointment.setContact_ID(contact_ID);
                appointment.setContact(contactName);
                allAppointments.add(appointment);

                appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
                contactColumn.setCellValueFactory(new PropertyValueFactory<>("Contact_Name"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
                startColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
                endColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
                customer_ID_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
                userIDColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        appointmentsTable.setItems(allAppointments);
        appointmentsTable.getSortOrder().add(startColumn);
    }

    /** The backToMain ActionEvent. This event is triggered when the user clicks on the back button in the customer screen
     * and will direct them back to the main screen. */
    public void backToMain(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /** The addCustomer() ActionEvent. This even is triggered when the user selects the add button under the customer
     * table. When clicked it will direct the user to the add customer screen. */
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/AddView.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /** The editCustomer() ActionEvent. This event is triggered when the customer clicks on the edit button below the
     * customer table. If there is no customer selected it will cause an alert to display letting the user know that no
     * selection has been made. If the user has selected a customer it will load the edit customer screen. */
    public void editCustomer(ActionEvent actionEvent) throws IOException {
        modifyCustomer = customerTable.getSelectionModel().getSelectedItem();
        selectedCustomer = modifyCustomer;

        if (modifyCustomer != null) {
            Parent root = FXMLLoader.load(getClass().getResource("../Views/EditView.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setTitle("Edit Customer");

            stage.setScene(scene);
            stage.show();
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No customer found");
            alert.setContentText("Please select a customer");
            alert.showAndWait();
        }
        System.out.println("This does nothing at the moment");
    }

    /** The deleteCustomer ActionEvent(). This event is triggered when the user selects the delete button underneath the
     * customer table. If a customer has not been selected, it will let the user know that they will need to select a
     * customer. If a customer is selected it will inform the user that all associated appointments for that customer
     * will be deleted and thanks to the selectAppointments method the user will be able to see all the appointments
     * that will be deleted. If the user decides that they want to continue a Delete statement is ran that will delete
     * the appointments associated with that customer first to avoid foreign key constraint errors and then delete the
     * customer from the database.*/
    public void deleteCustomer(ActionEvent actionEvent) throws IOException {
        modifyCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (modifyCustomer != null) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Customer: "+ modifyCustomer.getCustomer_Name() + ", and their associated\nappointments" +
                    " will be Deleted\nDo you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try{
                    int id = modifyCustomer.getCustomer_ID();
                    String deleteAppointmentsStatement = "DELETE FROM appointments WHERE customer_ID = ?";
                    DBQuery.setPreparedStatement(conn, deleteAppointmentsStatement);
                    PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                } catch (SQLException error) {
                    error.printStackTrace();
                }

                try {
                    int id = modifyCustomer.getCustomer_ID();
                    String deleteCustomerStatement = "DELETE FROM customers WHERE customer_ID = ?";
                    DBQuery.setPreparedStatement(conn, deleteCustomerStatement);
                    PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                } catch (SQLException error) {
                    error.printStackTrace();
                }
                getDBCustomers();
                getDBAppointments();
            }
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please select a customer.");
            alert.showAndWait();
        }
    }

    /** The appointmentAdd() ActionEvent. This event is triggered when the user clicks on the add button below the
     * Appointment table. When clicked the user will be directed to the add appointment screen. */
    public void appointmentAdd(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/Appointment.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /** The editAppointment() ActionEvent. This event is triggered when the user clicks on the edit button below the
     * appointment table. If no appointment is selected in the appointment table an alert will display letting the user
     * know that they need to make a selection. If there is an appointment selected the user will be directed to the
     * edit appointment screen.*/
    public void editAppointment(ActionEvent actionEvent) throws IOException {
        modifyAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        selectedAppointment = modifyAppointment;
        if (modifyAppointment != null) {
            Parent root = FXMLLoader.load(getClass().getResource("../Views/EditAppointment.fxml"));

            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setTitle("Appointment");
            stage.setScene(scene);
            stage.show();
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment found");
            alert.setContentText("Please select an appointment");
            alert.showAndWait();
        }
    }

    /** The deleteAppointment() ActionEvent. This event will be triggered when the user clicks on the delete button
     * below the appointment table. If no selection is made, the user will be notified that they need to make a selection.
     * If an appointment is selected the user will be informed that the appointment will be deleted and if they click
     * ok a delete statement will be ran deleting the appointment from the database with the matching appointment ID.*/
    public void deleteAppointment(ActionEvent actionEvent) throws IOException {
        modifyAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (modifyAppointment != null) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Appointment ID: " + modifyAppointment.getAppointment_ID() + "\nAppointment Type: "
                    + modifyAppointment.getType() + "\nWill be Deleted!\nDo you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try{
                    int id = modifyAppointment.getAppointment_ID();
                    String deleteAppointmentsStatement = "DELETE FROM appointments WHERE appointment_ID = ?";
                    DBQuery.setPreparedStatement(conn, deleteAppointmentsStatement);
                    PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                } catch (SQLException error) {
                    error.printStackTrace();
                }
                if (allAppointments.contains(modifyAppointment)) {
                    allAppointments.remove(modifyAppointment);
                }
            }
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select an appointment.");
            alert.showAndWait();
        }
    }

    /** getAllAppointments() ActionEvent. This event will be triggered when the user clicks on the get all appointments
     * button underneath the customer table. I have included this button in case the user wants to see all the
     * appointments when only showing the selected customers appointments. */
    public void getAllAppointments(ActionEvent actionEvent) {
        getDBAppointments();
        appointmentsTable.setItems(allAppointments);
        appointmentsTable.getSortOrder().add(startColumn);

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
        customer_ID_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
    }

    /** The weekView() ActionEvent. This event is tied to the radio button selections and will show the user the coming
     * appointments for the next week. It will call the getDBAppointments method and show only the appointments that
     * are happening in the next 7 days including the current day.*/
    public void weekView(ActionEvent actionEvent) throws Exception {
        LocalDateTime today = LocalDateTime.now();
        today = today.minusDays(1);
        DayOfWeek dow = today.getDayOfWeek();
        LocalDateTime comingWeek = today.plusDays(7);
        appointmentsTable.getItems().clear();
        getDBAppointments();
        ObservableList<Appointments> appointmentsByWeek = FXCollections.observableArrayList();

        for (Appointments appointment: allAppointments) {
            LocalDateTime apptStart = appointment.getLocalStart();
            if ((apptStart.isEqual(today) || apptStart.isAfter(today)) && apptStart.isBefore(comingWeek)) {
                appointmentsByWeek.add(appointment);
                appointmentsTable.setItems(appointmentsByWeek);
                appointmentsTable.getSortOrder().add(startColumn);

                appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
                startColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
                endColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
                customer_ID_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
                userIDColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
            }
        }
    }

    /** The monthView()ActionEvent. This event is tied to the radio button selections and will show the user the coming
     * appointments for the current month. It will call the getDBAppointments() method and find the current month by
     * running the LocalDateTime.now() method and then calling the getMonth() method on that variable. Once the month is
     * found it will go through a for loop looking through each appointment and adding any appointment to the
     * appointmentsByMonth Observable list and displaying it to the appointments table.*/
    public void monthView(ActionEvent actionEvent) throws Exception {
        LocalDateTime today = LocalDateTime.now();
        Month month = today.getMonth();
        appointmentsTable.getItems().clear();
        getDBAppointments();
        ObservableList<Appointments> appointmentsByMonth = FXCollections.observableArrayList();

        for (Appointments appointment: allAppointments) {
            LocalDateTime apptStart = appointment.getLocalStart();
            if (apptStart.getMonth().equals(month)) {
                appointmentsByMonth.add(appointment);
                appointmentsTable.setItems(appointmentsByMonth);
                appointmentsTable.getSortOrder().add(startColumn);

                appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
                titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
                startColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
                endColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
                customer_ID_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
                userIDColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
            }
        }
    }

    /** The allAppointmentView() ActionEvent. This event will be triggered when the user selects the all radio button.
     * I have included this so that the user can see all the available appointments instead of those in the current
     * week or month. This will simply just call the getDBAppointments() method and display all appointments to the
     * appointment table.*/
    public void allAppointmentView(ActionEvent actionEvent) throws Exception {
        getDBAppointments();
        appointmentsTable.setItems(allAppointments);
        appointmentsTable.getSortOrder().add(startColumn);

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
        customer_ID_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
    }

    /** The initialize method(). This method is part of the Initializable interface and will load the customer and
     * appointment data that is in the database and display that to the different tables. */
    public void initialize(URL location, ResourceBundle resources) {
        getDBCustomers();
        customerTable.setItems(Customers.getAllCustomers());

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customer_Name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        firstLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stateProv"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postal_Code"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        getDBAppointments();
        appointmentsTable.setItems(Appointments.allAppointments);


        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("Contact_Name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customer_ID_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
        appointmentsTable.getSortOrder().add(startColumn);
    }
}
