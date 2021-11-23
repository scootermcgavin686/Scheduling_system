package Controllers;

import Model.Contacts;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import static Model.Contacts.getDBContacts;


/** The ContactsController Class. This class implements the Initializable interface. This class is responsible for
 * controlling the actions taken on the contacts screen. This class will display the available contacts that are with
 * the company and their respective information. All reports for contacts are handled in the reports screen.*/
public class ContactsController implements Initializable{
    public TableView allContactsTable;
    public TableColumn contactIDColumn;
    public TableColumn contactNameColumn;
    public TableColumn emailAddressColumn;

    /** The backToMain() method. This method will load the MainView fxml file when the user clicks on the back button. */
    public void backToMain(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/MainView.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /** The initialize() method. This method is responsible for handling preparing the current screen with data. It will
     * load the contacts table with all contacts that are with the company.
     * @param location the location that is passed to the method.
     * @param resources the resource bundle that is passed to the method.*/
    public void initialize(URL location, ResourceBundle resources) {
        getDBContacts();
        allContactsTable.setItems(Contacts.getAllContacts());

        contactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contact_ID"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contact_Name"));
        emailAddressColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
}
