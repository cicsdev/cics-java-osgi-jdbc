package com.ibm.cicsdev.java.osgi.jdbc;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.cics.server.Task;
import com.ibm.cics.server.Terminal;
import com.ibm.cics.server.invocation.CICSProgram;
import com.ibm.cicsdev.java.osgi.jdbc.data.Employee;

/**
 * CICS-MainClass program class which reads data from a CICS Db2 connection
 * (DB2CONN) using JDBC.
 */
public class JDBCQueryProgram
{

    /** The URL of the Db2 instance */
    private static final String URL = "jdbc:default:connection";

    /** The Db2 schema to acccess. */
    private static final String SCHEMA = "DBADMIN";

    /** The query that will be run on the Db2 EMP database */
    private static final String QUERY = "select EMPNO, FIRSTNME, LASTNAME, SALARY from EMP";

    private static boolean initialized = false;

    /**
     * Opens the connection to Db2.
     * 
     * @return The {@link Connection} object.
     * @throws SQLException
     */
    private static Connection openConnection() throws SQLException
    {
        // Load DB2 JDBC driver
        if (!initialized)
        {
            try
            {
                Class.forName("com.ibm.db2.jcc.DB2Driver");
                initialized = true;
            }
            catch (ClassNotFoundException e)
            {
                throw new SQLException(e);
            }
        }

        Connection conn = DriverManager.getConnection(URL);
        conn.setSchema(SCHEMA);

        return conn;
    }

    /** The current CICS task */
    private final Task task;

    public JDBCQueryProgram()
    {
        this(Task.getTask());
    }

    /**
     * Creates a new instance of the JDBC query program.
     * <p>
     * A new instance must be created per link request as JCICS object cannot be
     * shared across threads. For more information, see
     * {@link com.ibm.cics.server.API}.
     * 
     * @param task
     *            The current task
     */
    public JDBCQueryProgram(Task task)
    {
        this.task = task;
    }

    /**
     * Runs the business logic of the program.
     * <ol>
     * <li>Opens a connection to the Db2 database using the URL
     * {@value #URL}.</li>
     * <li>Sets the current Db2 schema to {@value #SCHEMA}.</li>
     * <li>Executes the SQL query {@value #QUERY}.</li>
     * <li>Marshalls the data from the query into an {@link Employee}
     * object.</li>
     * <li>Prints the employees in tabular format to the terminal and
     * STDOUT.</li>
     * </ol>
     * 
     * @throws SQLException
     */
    @CICSProgram("CDEVJODB")
    public void run() throws SQLException
    {
        // Query the database for the employees
        List<Employee> employees = getEmployees();

        Object principalFacility = task.getPrincipalFacility();
        boolean isTerminal = principalFacility != null && principalFacility instanceof Terminal;

        // Print the employees in tabular format to the terminal.
        try (PrintWriter out = task.getOut())
        {
            out.println();

            int count = 0;
            for(Employee employee : employees)
            {
                // Print the first 15 employees to the terminal, if available.
                if(count < 15 && isTerminal)
                {
                    out.println(employee);
                }

                // Print out to STDOUT.
                System.out.println(employee);

                count++;
            }
        }

    }

    /**
     * Gets a list of employee from the database.
     * 
     * @return A list of employee.
     * @throws SQLException
     *             If database interaction fails.
     */
    private List<Employee> getEmployees() throws SQLException
    {
        List<Employee> employees = new ArrayList<>();

        // Open to Db2 connection
        try (Connection conn = openConnection())
        {
            // Retrieve the data from the database.
            try (PreparedStatement statement = conn.prepareStatement(QUERY))
            {
                ResultSet results = statement.executeQuery();

                // Add each employees to the list
                while (results.next())
                {
                    Employee employee = getEmployee(results);
                    employees.add(employee);
                }
            }
        }

        return employees;
    }

    /**
     * Marshalls the data from a ResultSet into an {@link Employee} object.
     * 
     * @param result
     *            The ResultSet to load the data from, with the cursor on the
     *            current row.
     * @return An Employee object with all field populated.
     * @throws SQLException
     *             If the employee columns are not accessible.
     */
    private Employee getEmployee(ResultSet result) throws SQLException
    {
        String empno = result.getString("EMPNO");
        String firstName = result.getString("FIRSTNME");
        String lastName = result.getString("LASTNAME");
        BigDecimal salary = result.getBigDecimal("SALARY");

        return new Employee(empno, firstName, lastName, salary);
    }
}
