package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;

/** The MainController Class. This class handles all actions that occur on the main screen once the user logs in. */
public class MainController {
    Alert alert = new Alert(Alert.AlertType.NONE);

    @FXML
    Button customers;
    @FXML
    Button contacts;

    /** The customerSelect() ActionEvent. This event is triggered when the user clicks on the customer button and will
     * direct them to the Customerview screen by loading the CustomerView FXML file. */
    public void customerSelect(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/CustomerView.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

    /** The contactsSelect() ActionEvent. This event is triggered when the user clicks on the contacts button and will
     * direct them to the ContactsView screen by loading the ContactsView FXML file. */
    public void contactsSelect(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/ContactsView.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

    /** The Reports() ActionEvent. This event is triggered when the user clicks on the Reports button and will
     * direct them to the Reports screen by loading the Reports FXML file. */
    public void Reports(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/Reports.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    /** The exit() ActionEvent. This event is triggered when the user clicks on the exit button. This will alert the user
     * that it will end the application and if the user selects ok it will end the program by calling the System.exit
     * method.*/
    public void exit(ActionEvent actionEvent) throws Exception {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("This will end the program, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
