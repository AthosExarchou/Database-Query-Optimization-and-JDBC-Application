/* imports */
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBApplication {

    /* connects to the HUA Oracle Data Base */
    public static Connection getConnection() throws SQLException {

        Properties props = new Properties();

        //reads the DB credentials in the 'db.properties' file
        try (InputStream input = new FileInputStream("db.properties")) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String url = props.getProperty("db.url"); //DB url
        String user = props.getProperty("db.user"); //DB username
        String password = props.getProperty("db.password"); //DB password

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //establishes the connection
        return DriverManager.getConnection(url, user, password);
    }

    /* prints all the employees that meet the set condition */
    public void fetchAllEmployeeDepartmentPrepared(Connection connection) throws SQLException {

        String sql = "SELECT fname, lname, dname FROM employee " +
                        "JOIN department ON dno=dnumber";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
            
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String dname = rs.getString("dname");

                System.out.println(fname + " | " + lname + " | " + dname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* prints the explain plan for the executed query */
    public void createExplainPlan(Connection connection) throws SQLException {

        String sqlPlan = "EXPLAIN PLAN FOR SELECT fname, lname, dname " +
                            "FROM employee JOIN department ON dno=dnumber";
        String sqlPrintPlan = "SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY)";

        try (PreparedStatement pstmt1 = connection.prepareStatement(sqlPlan)) {
    
            pstmt1.execute(); //executes the explain plan
        } catch (SQLException e) {
            e.printStackTrace();
        }
            
        /* displays the explain plan */
        try (PreparedStatement pstmt2 = connection.prepareStatement(sqlPrintPlan);
             ResultSet rs = pstmt2.executeQuery()) {

                System.out.println("EXPLAIN PLAN:");
            while (rs.next()) {
            
                String explPlan = rs.getString(1);
                System.out.println(explPlan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* creates the 'index_employee_dno' INDEX */
    public void createIndex(Connection connection) throws SQLException {
        //creates the index on the 'dno' column of EMPLOYEE
        String crIndexQ = "CREATE INDEX index_employee_dno ON EMPLOYEE(dno)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(crIndexQ);
            System.out.println("Index 'index_employee_dno' created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* deletes the 'index_employee_dno' INDEX */
    public void deleteIndex(Connection connection) throws SQLException {

        String dropIndexQ = "DROP INDEX index_employee_dno";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(dropIndexQ);
            System.out.println("Index 'index_employee_dno' deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Index 'index_employee_dno' does not exist, hence unable to delete.");
        }
    }

    /*
     *  Connects to the HUA Oracle DB and accesses the "Company" DB
     *  that was created in the labs of "Baseis Dedomenwn".
     *  The connection is automatically closed afterwards. 
     *  (similar implementation to that of the labs)
    */
    public static void main(String[] args) {

        try (Connection connection = DBApplication.getConnection()) {
            
            System.out.println("------------------------------------------------------------------");
            System.out.println("Successfully connected to the HUA Oracle DB!");
            System.out.println("------------------------------------------------------------------");
            new DBApplication().deleteIndex(connection);
            System.out.println("------------------------------------------------------------------");
            System.out.println("EMPLOYEE LIST:");
            new DBApplication().fetchAllEmployeeDepartmentPrepared(connection);
            System.out.println("------------------------------------------------------------------");
            new DBApplication().createExplainPlan(connection);
            System.out.println("---------------------------------------------------");
            new DBApplication().createIndex(connection);
            System.out.println("---------------------------------------------------");
            new DBApplication().createExplainPlan(connection);
            System.out.println("---------------------------------------------------");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
