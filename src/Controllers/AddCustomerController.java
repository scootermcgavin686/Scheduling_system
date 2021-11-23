package Controllers;

import DBAccess.DBQuery;
import Model.Countries;
import Model.FirstLevelDivisions;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import static DBAccess.DBConnect.conn;
import static Model.Countries.getDBCountries;
import static Model.Customers.getDBCustomers;
import static Model.FirstLevelDivisions.getDBFirstLevel;
import static Model.FirstLevelDivisions.statesProv;

/** The AddCustomerController class. This class is responsible for handling the action events that are associated with
 * the Add Customer screen.  */
public class AddCustomerController implements Initializable {
    Alert alert = new Alert(Alert.AlertType.NONE);

    @FXML
    Label menuLabel;
    @FXML
    TextField nameField;
    @FXML
    TextField addressField;
    @FXML
    TextField postalField;
    @FXML
    TextField phoneField;
    @FXML
    TextField customerIDField;

    public ComboBox<Countries> countryCombo;
    public ComboBox<FirstLevelDivisions> firstLevelCombo;


    /** The backToCustomer() ActionEvent. The action event will throw an Exception if the FXMLLoader cannot find the
     * CustomerView FXML file. The method will ask the user if they want to go back to the Customer main screen and let
     * them know that any information will not be saved.*/
    public void backToCustomer(ActionEvent actionEvent) throws Exception {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("The Customer will not be saved,\nDo you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try{
                Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setTitle("Customer");
                stage.setScene(scene);
                stage.show();
            } catch (NullPointerException error) {
                error.printStackTrace();
            }
        }
    }

    /** The saveCustomer() ActionEvent. This event will throw an Exception if the CustomerView fxml file cannot be found.
     * This class will run the validateCustomer method and if that method returns true it will gather all the data from
     * the input fields. It will input this data into an Insert statement and upload it to the database.*/
    public void saveCustomer(ActionEvent actionEvent) throws Exception {
        boolean isValid = true;

        try {
            if (validateCustomer()) {
                FirstLevelDivisions firstLevel = firstLevelCombo.getSelectionModel().getSelectedItem();
                // Countries country = countryCombo.getSelectionModel().getSelectedItem();

                String CustomerName = nameField.getText();
                String Address = addressField.getText();
                String PostalCode = postalField.getText().toUpperCase(Locale.ROOT);
                String Phone = phoneField.getText();
                int DivisionID = firstLevel.getDivision_ID();

                String insertStatement = "INSERT INTO customers (customer_Name, address, postal_Code, phone, division_ID) " +
                        "VALUES (?, ?, ?, ?, ?)";

                DBQuery.setPreparedStatement(conn, insertStatement);
                PreparedStatement preparedStatement = DBQuery.getPreparedStatement();

                preparedStatement.setString(1, CustomerName);
                preparedStatement.setString(2, Address);
                preparedStatement.setString(3, PostalCode);
                preparedStatement.setString(4, Phone);
                preparedStatement.setInt(5, DivisionID);
                preparedStatement.execute();

                getDBCustomers();

                isValid = true;
            } else {
                isValid = false;
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }

        if (isValid) {
            Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setTitle("Customer");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** The selectedCountry() ActionEvent. When the user selects a country in the country combo box it will run the
     * getDBFirstLevel method which takes an input of the countryID that was selected. It will create a list that populates
     * the firstLevelCombo box.*/
    public void selectedCountry(ActionEvent actionEvent) throws Exception {
        Countries selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            int countryID = selectedCountry.getCountry_ID();
            getDBFirstLevel(countryID);

            firstLevelCombo.setItems(statesProv);
        }
    }

    /** The validateCustomer() method. This method will verify that data has been entered in the text fields and that
     * selections have been made on the combo boxes. If any errors have been found a StringBuilder class collects all errors
     * and will display that in an error alert.*/
    public boolean validateCustomer() {
        StringBuilder errors = new StringBuilder();

        try {
            if (nameField.getText().trim().isEmpty()) {
                errors.append("- Enter a Customer Name. \n");
            }
            if (addressField.getText().trim().isEmpty()) {
                errors.append("- Enter an Address. \n");
            }
            if ((postalField.getText().trim().isEmpty()) || (postalField.getText().length()) != 5) {
                errors.append("- Enter a 5 digit Postal Code. \n");
            }
            if (phoneField.getText().trim().isEmpty() || (isPhone(phoneField.getText()))) {
                errors.append("- Enter a Phone Number. \n");
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

    /** The isPhone() method. This method is used to verify that a phone number containing only numbers and symbols has been
     * entered.*/
    public boolean isPhone(String phoneNumber) {
        try {
            if (phoneNumber.matches(".*[a-z].*")) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException error) {
            return false;
        }
    }

    // For future updates to verify postal field is alphanumeric. Was told not needed for project.
//    public boolean isAlphaNum(String s) {
//        boolean match = s.matches("[a-zA-Z0-9]+");
//        if(match) {return true;}
//        else {return false;}
//    }

    /** The initialize() method. This method is responsible for running database calls and loading the information into
     * combo boxes.*/
    public void initialize(URL location, ResourceBundle resources) {
        getDBCountries();
        countryCombo.setItems(Countries.allCountries);
        countryCombo.setVisibleRowCount(3);
        firstLevelCombo.setItems(statesProv);
    }

}
