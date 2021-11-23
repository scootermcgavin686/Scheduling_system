package DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The DBConnect class creates a Connection class as well as assigning variables to make the connection to the database
 * possible.
 */
public abstract class DBConnect {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection conn;  // Connection Interface

    /**
     * The openConnection() method. This is the method responsible for opening the connection with the database. It
     * locates the driver for the database connection, creates a connection through the use of a lambda, and generates
     * that the connection was successful in the console.
     * */
    public static Connection openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            conn = (Connection) DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
        return conn;
    }

    /**
     * The closeConnection() method. This is the method responsible for closing the connection to the database. Will call the connection objects method
     * .close() to end the connection.
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
