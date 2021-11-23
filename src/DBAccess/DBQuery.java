package DBAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The DBQuery Class. This class is responsible for handling query statements between the java application and the MySQL
 * database. I have included methods for both statements and prepared statements.
 */
public class DBQuery {
    public static Statement statement;
    public static PreparedStatement preparedStatement;

    // Create statement object
    /** The setStatement() method. This takes a parameter of the Connection class called conn and calls a createStatement()
     * method.
     * @param conn The connection object created in the DBConnect class.*/
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    // Return statement object
    /** The getStatement() method. This method is called to get the statement object created in the set statement method.*/
    public static Statement getStatement(){
        return statement;
    }

    /** The setPreparedStatement() method. This method is used to create a prepared statement object and will accept two
     * parameters. One being a connection object and the other a string where a SQL statement will be passed.
     * @param conn The connection object passed to the method
     * @param sqlStatement The sqlStatement that is passed is a String that will have a functioning SQL statement.*/
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        preparedStatement = conn.prepareStatement(sqlStatement);
    }

    /** The getPreparedStatement() method. This method will be the one responsible for getting the statement object that
     * was created in the setPreparedStatement() method. */
    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
