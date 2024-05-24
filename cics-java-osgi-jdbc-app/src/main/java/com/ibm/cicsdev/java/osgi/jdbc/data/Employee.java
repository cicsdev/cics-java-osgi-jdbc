package com.ibm.cicsdev.java.osgi.jdbc.data;

import java.math.BigDecimal;

/**
 * A partial object representation of an employee in the Employee table
 * (DSN8D10.EMP), as defoined below.
 * 
 * <pre>
 * CREATE TABLE DSN8D10.EMP
 *       (EMPNO     CHAR(6)                          NOT NULL,
 *        FIRSTNME  VARCHAR(12)                      NOT NULL,
 *        MIDINIT   CHAR(1)                          NOT NULL,
 *        LASTNAME  VARCHAR(15)                      NOT NULL,
 *        WORKDEPT  CHAR(3)                                  ,
 *        PHONENO   CHAR(4)         CONSTRAINT NUMBER CHECK
 *                  (PHONENO >= '0000' AND
 *                   PHONENO <= '9999')                      ,
 *        HIREDATE  DATE                                     ,
 *        JOB       CHAR(8)                                  ,
 *        EDLEVEL   SMALLINT                                 ,
 *        SEX       CHAR(1)                                  ,
 *        BIRTHDATE DATE                                     ,
 *        SALARY    DECIMAL(9,2)                             ,
 *        BONUS     DECIMAL(9,2)                             ,
 *        COMM      DECIMAL(9,2)                             ,
 *        PRIMARY KEY (EMPNO)                                ,
 *        FOREIGN KEY RED (WORKDEPT) REFERENCES DSN8D10.DEPT
 *                  ON DELETE SET NULL                       )
 *   EDITPROC  DSN8EAE1
 *   IN DSN8D13A.DSN8S13E
 *   CCSID EBCDIC;
 * </pre>
 * 
 * For more information, see 
 * <a href="https://www.ibm.com/docs/en/db2-for-zos/13?topic=tables-employee-table-dsn8d10emp">Employee table (DSN8D10.EMP)</a>.
 */
public class Employee
{
    /**
     * Pads a string to an exact length
     * 
     * @param input
     *            The input string
     * @param length
     *            The length to pad to.
     * @return The padded string.
     */
    private static final String pad(String input, int length)
    {
        return String.format("%" + length + "s", input);
    }

    /** The employee number - EMPNO */
    private String employeeNumber;

    /** The employees first name - FIRSTNME */
    private String firstName;

    /** The employees last name - LASTNAME */
    private String lastName;

    /** The employees salary - SALARY */
    private BigDecimal salary;

    /**
     * @param employeeNumber
     *            The employees number
     * @param firstName
     *            The first name
     * @param lastName
     *            The last name
     * @param salary
     *            The salary
     */
    public Employee(String employeeNumber, String firstName, String lastName, BigDecimal salary)
    {
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    @Override
    public String toString()
    {
        return employeeNumber + " " + pad(firstName, 12) + " " + pad(lastName, 15) + " " + pad(salary.toString(), 11);
    }
}
