package Model;

import DBAccess.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

import static DBAccess.DBConnect.conn;

/** The Reports class. This class creates the variables used in the reports controller class. */
public class Reports {
    ObservableList<Appointments> listOfAppointments = FXCollections.observableArrayList();
    public static ObservableList<String> types = FXCollections.observableArrayList();
    public static ObservableList<Month> months = FXCollections.observableArrayList();

    /** The getTypes() method. This method will go gather distinct types for every appointment in the database. It will
     * then add this data to an observable list that is used to populate a combo box in the Reports controller. */
    public static void getTypes() {
        types.clear();
        String selectTypes = "SELECT DISTINCT Type FROM appointments";

        try {
            DBQuery.setPreparedStatement(conn, selectTypes);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                types.add(type);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /** The setMonths() method. This method sets Month ENUM values into an observable list for the months combo box in
     * the reports controller class. */
    public static void setMonths() {
        months.add(Month.valueOf("JANUARY"));
        months.add(Month.valueOf("FEBRUARY"));
        months.add(Month.valueOf("MARCH"));
        months.add(Month.valueOf("APRIL"));
        months.add(Month.valueOf("MAY"));
        months.add(Month.valueOf("JUNE"));
        months.add(Month.valueOf("JULY"));
        months.add(Month.valueOf("AUGUST"));
        months.add(Month.valueOf("SEPTEMBER"));
        months.add(Month.valueOf("OCTOBER"));
        months.add(Month.valueOf("NOVEMBER"));
        months.add(Month.valueOf("DECEMBER"));
    }
}
