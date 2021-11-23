package Controllers;

import DBAccess.DBQuery;
import DBAccess.TimeConversion;
import Model.Appointments;
import Model.Contacts;
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
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import static DBAccess.DBConnect.conn;
import static DBAccess.TimeConversion.timeStampToLocal;
import static Model.Appointments.allAppointments;
import static Model.Appointments.getDBAppointments;
import static Model.Contacts.allContacts;
import static Model.Contacts.getDBContacts;
import static Model.Reports.*;

/** The ReportsController class is responsible for handling all the actions on the Reports screen. This class implements
 * the Initializable interface. */
public class ReportsController implements Initializable {
    public ComboBox<String> typeCombo;
    public ComboBox<Month> monthCombo;
    public ComboBox<Contacts> contactsCombo;

    int numOfAppointments = 0;
    String selectedType;

    @FXML
    Label numberOfAppointments;

    public TableView<Appointments> appointmentTable;
    public TableColumn<Appointments, Integer> appointmentID;
    public TableColumn<Appointments, String> title;
    public TableColumn<Appointments, String> type;
    public TableColumn<Appointments, String> description;
    public TableColumn<Appointments, LocalDateTime> start;
    public TableColumn<Appointments, LocalDateTime> end;
    public TableColumn<Appointments, Integer> customerID;

    /** The searchAppt() method. This action event runs the getDBAppointment() method and then searches those appointments
     * for the matching type and month chosen in the respective combo boxes. A variable will increase everytime that there
     * is a match. It will then populate a label with a string displaying the amount of matches when the find button is
     * clicked. */
    public void searchAppt(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointments> report = FXCollections.observableArrayList();
        String select = "SELECT start, type FROM appointments";
        Statement statement = DBQuery.getStatement();
        try {
            statement.execute(select);
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                String dbType = rs.getString("type");
                Timestamp dbStart = rs.getTimestamp("start");
                LocalDateTime localStart = TimeConversion.timeStampToLocal(dbStart);
                Appointments a = new Appointments();
                a.setType(dbType);
                a.setLocalStart(localStart);
                report.add(a);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        if(!monthCombo.getSelectionModel().isEmpty() && !typeCombo.getSelectionModel().isEmpty()) {
            for (Appointments appointment : report) {
                Month apptMonth = appointment.getLocalStart().getMonth();
                Month selectedMonth = monthCombo.getSelectionModel().getSelectedItem();
                String type = appointment.getType();
                selectedType = typeCombo.getSelectionModel().getSelectedItem();

                if ((apptMonth.equals(selectedMonth)) && (type.equals(selectedType))) {
                    numOfAppointments++;
                }
            }
            numberOfAppointments.setText("The number of  " + selectedType
                    + " appointments in " + monthCombo.getSelectionModel().getSelectedItem().toString()
                    + " is " + numOfAppointments);
            numOfAppointments = 0;
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Please select an appointment type and month.");
            a.showAndWait();
        }
    }

    /** The back() Method. This will guide the user back to the main screen when the back button is clicked.*/
    public void back(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /** The ListOFAppointments() method. This will create a select statement that will choose all the appointments that
     * match the selected Contacts ID. It will then load the date into the table below. */
    public void ListOFAppointments() {
        if (!contactsCombo.getSelectionModel().isEmpty()) {
            String selectContactAppt = "SELECT * FROM appointments WHERE appointments.Contact_ID = ?";
            Contacts contact = contactsCombo.getSelectionModel().getSelectedItem();
            int contactID = contact.getContact_ID();
            allAppointments.clear();

            try {
                DBQuery.setPreparedStatement(conn, selectContactAppt);
                PreparedStatement ps = DBQuery.getPreparedStatement();
                ps.setInt(1, contactID);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int dbAppointmentID = rs.getInt("Appointment_ID");
                    String dbTitle = rs.getString("Title");
                    String dbType = rs.getString("Type");
                    String Description = rs.getString("Description");
                    Timestamp dbStart = rs.getTimestamp("Start");
                    Timestamp dbEnd = rs.getTimestamp("End");
                    int dbCustomerID = rs.getInt("Customer_ID");
                    Appointments appointments = new Appointments();
                    appointments.setAppointment_ID(dbAppointmentID);
                    appointments.setTitle(dbTitle);
                    appointments.setLocalStart(timeStampToLocal(dbStart));
                    appointments.setLocalEnd(timeStampToLocal(dbEnd));
                    appointments.setType(dbType);
                    appointments.setDescription(Description);
                    appointments.setCustomer_ID(dbCustomerID);
                    allAppointments.add(appointments);
                }
                appointmentTable.setItems(allAppointments);
                appointmentTable.getSortOrder().add(start);

                appointmentID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
                title.setCellValueFactory(new PropertyValueFactory<>("Title"));
                type.setCellValueFactory(new PropertyValueFactory<>("Type"));
                description.setCellValueFactory(new PropertyValueFactory<>("Description"));
                start.setCellValueFactory(new PropertyValueFactory<>("LocalStart"));
                end.setCellValueFactory(new PropertyValueFactory<>("LocalEnd"));
                customerID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));

            } catch (SQLException error) {
                error.printStackTrace();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText("Please select a contact.");
            a.showAndWait();
        }
    }

    /** The initialize() method. This method is responsible for running the getTypes(), setMonths() and getDBContacts()
     * methods. It will then populate the comboboxes with the appropriate date. */
    public void initialize(URL location, ResourceBundle resources) {
        getTypes();
        setMonths();
        getDBContacts();
        typeCombo.setItems(types);
        monthCombo.setItems(months);
        contactsCombo.setItems(allContacts);
    }
}
