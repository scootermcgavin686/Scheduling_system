package Main;

import DBAccess.DBConnect;
import DBAccess.DBQuery;
import Util.Data_Manager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The main class is responsible for the loading and initialization of the project files. The program will create an
 * application for scheduling appointments with customers and assigning contacts for each appointment. The main class
 * will make a connection to the MySQL database so that the application can interact with it throughout.
 *
 * @author Scott Moore
 */
public class Main extends Application {

    /** The start method. This will load the files to take the user to the originating screen.
     *
     * @param primaryStage The primary stage is set and takes the user to the login screen.
     * @throws Exception Throws Exception if the file cannot be found.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Data_Manager.rb = ResourceBundle.getBundle("Resources/Nat", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("../Views/LoginView.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(Data_Manager.rb.getString("Scheduling"));
        primaryStage.show();
    }

    /**
     * The main method. This will be the method responsible for making a connection to the database by creating the Connection
     * object conn and assigning it
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {

        // This code is responsible for making a connection with the database by creating a Connection object.
        Connection conn = DBConnect.openConnection();
        DBQuery.setStatement(conn);

        launch(args);

        DBConnect.closeConnection();
    }
}
