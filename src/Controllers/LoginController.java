package Controllers;

import DBAccess.TimeConversion;
import Model.Appointments;
import Model.Login;
import Model.Users;
import Util.Data_Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.*;

import static Model.Appointments.getDBAppointments;
import static Model.Customers.getDBCustomers;

public class LoginController implements Initializable {
    Alert alert = new Alert(Alert.AlertType.NONE);

    Locale currentLocale = Locale.getDefault();
    String country = currentLocale.getCountry();
    String language = currentLocale.getLanguage();
    ZoneId userLocation = ZoneId.systemDefault();

    @FXML
    Button loginButton;

    @FXML
    TextField userID;
    @FXML
    TextField password;

    @FXML
    Label countryLocation;
    @FXML
    Label loginLabel;
    @FXML
    Label userIDLabel;
    @FXML
    Label passWordLabel;


    /** The userLogin() ActionEvent. This event is triggered when the user clicks on the login button on the main screen.
     * it will get the text that has been entered into the textfields and then check to see if there is any matching users
     * in the database. If it does find a user in the database matching the given username, it will then verify that the
     * password is also correct.*/
    public void userLogin(ActionEvent actionEvent) throws Exception {
        String User = userID.getText();
        String Password = password.getText();
        String successFail;


        if (Login.ULogin(User, Password) == true) {
            successFail = "Success";
            fileWrite(User, Password, successFail);
            Parent root = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));

            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            successFail = "Failed Login";
            fileWrite(User, Password, successFail);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle(Data_Manager.rb.getString("Error"));
            alert.setHeaderText(Data_Manager.rb.getString("Failed"));
            alert.setContentText(Data_Manager.rb.getString("contentText"));
            alert.showAndWait();
        }
    }

    /** The fileWrite() method. This method is responsible for loading the login activity file and writing to it. When
     * a user logs in, whether it is successful or not, this method will write a line to the file. It checks to see what
     * the local time is and will write the username, password, and whether it succeeded or failed when trying to login
     * to the application.*/
    public void fileWrite(String user, String password, String success_Fail) {
        File log = new File("Main.login_activity.txt");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate date = TimeConversion.localTimeDateToDate(localDateTime);
        LocalTime time = TimeConversion.localTimeDateToTime(localDateTime);

        try {
            FileWriter loginWriter = new FileWriter(log, true);
            loginWriter.write("USER: " + user + " | PASSWORD: " + password + " | LOGIN RESULT: " + success_Fail +
                    " | DATE: " + date + " | TIME: " + time + "\n");
            loginWriter.close();
            System.out.println("Successful write to file");
        } catch (IOException e) {
            System.out.println("An error has occurred while trying to write to file");
            e.printStackTrace();
        }
    }

    /** The initialize() method. This method is responsible for displaying the text into the labels which uses the key
     * value pairs in the resources bundle Nat. */
    public void initialize (URL location, ResourceBundle resources) {
        getDBCustomers();
        getDBAppointments();

        loginLabel.setText(Data_Manager.rb.getString("Login"));
        loginButton.setText(Data_Manager.rb.getString("Login"));
        userIDLabel.setText(Data_Manager.rb.getString("UserID"));
        passWordLabel.setText(Data_Manager.rb.getString("password"));
        countryLocation.setText(String.valueOf(ZoneId.systemDefault()));
    }
}
