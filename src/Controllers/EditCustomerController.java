package Controllers;

import DBAccess.DBQuery;
import Model.Countries;
import Model.Customers;
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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import static DBAccess.DBConnect.conn;
import static Model.Countries.getDBCountries;
import static Controllers.CustomerController.selectedCustomer;
import static Model.FirstLevelDivisions.getDBFirstLevel;
import static Model.FirstLevelDivisions.statesProv;

/** The EditCustomerController class. This class is responsible for handling the action events that are associated with
 * the Edit Customer screen.  */
public class EditCustomerController implements Initializable {
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
        alert.setTitle("Quit");
        alert.setContentText("Changes to the customer will not be saved\nDo you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setTitle("Customer");
            stage.setScene(scene);
            stage.show();
        }

    }

    /** The saveCustomer() ActionEvent. This event will throw an Exception if the CustomerView fxml file cannot be found.
     * This class will run the validateCustomer method and if that method returns true it will gather all the data from
     * the input fields. It will input this data into an Update statement and upload the changes to the database.*/
    public void saveCustomer(ActionEvent actionEvent) throws Exception {
        try {
            if (validateCustomer()) {
                FirstLevelDivisions firstLevel = firstLevelCombo.getSelectionModel().getSelectedItem();
                Countries country = countryCombo.getSelectionModel().getSelectedItem();

                int CustomerID = Integer.parseInt(customerIDField.getText());
                String CustomerName = nameField.getText();
                String Address = addressField.getText();
                String PostalCode = postalField.getText();
                String Phone = phoneField.getText();
                int DivisionID = firstLevel.getDivision_ID();
                String Division = firstLevel.getDivision();
                String Country = country.getCountry();

                String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                        "Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

                DBQuery.setPreparedStatement(conn, updateStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();

                ps.setString(1,CustomerName);
                ps.setString(2, Address);
                ps.setString(3, PostalCode);
                ps.setString(4, Phone);
                ps.setInt(5, DivisionID);
                ps.setInt(6, selectedCustomer.getCustomer_ID());
                selectedCustomer.setCountry(Country);
                selectedCustomer.setStateProv(Division);
                ps.execute();


                Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setTitle("Customer");
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /** The validateCustomer() method. This method will verify that data has been entered in the text fields and that
     * selections have been made on the combo boxes. If any errors have been found a StringBuilder class collects all error
     * messages and will display that in an error alert.*/
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
                errors.append("- Enter a valid phone number. \n");
            }
            if (countryCombo.getSelectionModel().isEmpty()) {
                errors.append("- Please select a country.\n");
            }
            if (firstLevelCombo.getSelectionModel().isEmpty()) {
                errors.append("- Please select a state.\n");
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

    @Override
    /** The initialize() method. This method is responsible for running database calls and loading the information into
     * combo boxes and textfields.*/
    public void initialize(URL location, ResourceBundle resources) {
        getDBCountries();
        getDBFirstLevel(selectedCustomer.getCountry_ID());

        nameField.setText(selectedCustomer.getCustomer_Name());
        addressField.setText(selectedCustomer.getAddress());
        postalField.setText(selectedCustomer.getPostal_Code());
        phoneField.setText(selectedCustomer.getPhone());
        customerIDField.setText(String.valueOf(selectedCustomer.getCustomer_ID()));

        countryCombo.setItems(Countries.allCountries);
        firstLevelCombo.setItems(statesProv);
        countryCombo.setVisibleRowCount(3);
        firstLevelCombo.setVisibleRowCount(5);

        for(Countries country: countryCombo.getItems()) {
            if(selectedCustomer.getCountry_ID() == country.getCountry_ID()) {
                countryCombo.setValue(country);
                break;
            }
        }

        for(FirstLevelDivisions FirstLevel: firstLevelCombo.getItems()) {
            if(FirstLevel.getDivision_ID() == selectedCustomer.getDivision_ID()) {
                firstLevelCombo.setValue(FirstLevel);
                break;
            }
        }
    }
}
