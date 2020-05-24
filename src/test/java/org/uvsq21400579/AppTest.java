package org.uvsq21400579;

import static org.junit.Assert.assertEquals;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uvsq21400579.Employee.Builder;
import java.sql.Connection;

public class AppTest {

    /**
     * Creates the embedded database required by some tests
     */
    @BeforeClass
    public static void initializeEmbeddedDB() {

        String createEmployeeTableString = "CREATE TABLE \"EMPLOYEE\"(NAME VARCHAR (128) NOT NULL, SURNAME VARCHAR(128) NOT NULL, PRIMARY KEY (NAME) , \"FUNCTION\" VARCHAR(128), PHONE VARCHAR(128), BIRTHDATE VARCHAR(128))";
        String createGroupTableString = "CREATE TABLE \"GROUP\"(NAME VARCHAR (128) PRIMARY KEY NOT NULL)";
        String createGroupDirectoryTableString = "CREATE TABLE GROUPDIRECTORY(NAME VARCHAR(128) PRIMARY KEY, FOREIGN KEY (NAME) REFERENCES \"GROUP\"(NAME))";
        String createDirectoryTableString = "CREATE TABLE DIRECTORY(NAME VARCHAR(128) PRIMARY KEY, FOREIGN KEY (NAME) REFERENCES EMPLOYEE(NAME))";
        String createGroupMembersTableString = "CREATE TABLE GROUPMEMBERS(GROUPNAME VARCHAR(128), FOREIGN KEY (GROUPNAME) REFERENCES \"GROUP\"(NAME), EMPLOYEENAME VARCHAR(128), FOREIGN KEY (EMPLOYEENAME) REFERENCES EMPLOYEE(NAME), PRIMARY KEY (GROUPNAME, EMPLOYEENAME))";
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String protocol = "jdbc:derby:";
        try{
            Class.forName(driver).newInstance();
            Connection connection = DriverManager.getConnection(protocol + "derbyDB;create=true");
            Statement statement = connection.createStatement();
            statement.execute(createEmployeeTableString);
            statement.execute(createGroupTableString);
            statement.execute(createGroupDirectoryTableString);
            statement.execute(createDirectoryTableString);
            statement.execute(createGroupMembersTableString);

            connection.close();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drops tables from the database so that between reruns the database is in a default state
     * @throws SQLException
     */
    @AfterClass
    public static void shutdownEmbeddedDB() throws SQLException {
        String cleanupEmployeeString = "DROP TABLE \"EMPLOYEE\"";
        String cleanupGroupString = "DROP TABLE \"GROUP\"";
        String cleanupGroupDirectoryString = "DROP TABLE GROUPDIRECTORY";
        String cleanupDirectoryString = "DROP TABLE DIRECTORY";
        String cleanupGroupMembersString = "DROP TABLE GROUPMEMBERS";

        Connection connection = DriverManager.getConnection("jdbc:derby:derbyDB");
        Statement statement = connection.createStatement();

        statement.execute(cleanupGroupMembersString);
        statement.execute(cleanupDirectoryString);
        statement.execute(cleanupGroupDirectoryString);
        statement.execute(cleanupEmployeeString);
        statement.execute(cleanupGroupString);
        connection.close();
    }

    @Test
    public void testEmployee() {
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").build();
        assertEquals("Dylan", employee.getName());
    }

    @Test
    public void testEmployeeDate() {
        LocalDate localDate = LocalDate.now();
        Employee employee = new Builder("Dylan", "Bob", "Singer").updateBirthDate(localDate).build();
        assertEquals(LocalDate.now(), employee.getBirthDate());
    }

    @Test
    public void testEmployeePhone() {
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        String phone = "0324657321";
        tmp.add(phone);
        tmp.add("312576865");
        assertEquals("Dylan", employee.getName());
        assertEquals("0324657321", employee.getPhone().get(0));
        assertEquals("312576865", employee.getPhone().get(1));
    }

    @Test
    public void testIterator() {
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        Directory directory = Directory.getInstance();
        directory.addTeam(employee);
        directory.addTeam(new Group("group_1"));
        directory.addTeam(new Group("group_2"));
        System.out.println("Start");
        for(Team e : directory) {
            e.printName();
        }
    }

    @Test
    public void testNode() {
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        Node<Team> Team_1 = new Node<>(employee);
        Team_1.addElement(new Group("group_1"));
        Team_1.addElement(employee);
        Team_1.addElement(new Group("group_2"));
        Node<Team> iterator = Team_1;
        while(iterator.hasNext()) {
            iterator.getElement().printName();
            iterator = iterator.getNext();
        }
    }


    @Test
    public void testIteratorGroup() {
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        Group group = new Group("Test");
        group.addMember(employee);
        group.addMember(new Group("group_2"));
        for(Team team : group) {
            team.printName();
        }
    }


    /**
     * Test for Directory, adding employees, teams and groups
     */
    @Test
    public void testDirectory() {
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        Employee employee2 = new Employee.Builder("Hendrix", "Jimmy", "GuitarHero").updatePhoneList(tmp).build();
        Directory directory = Directory.getInstance();
        Group group_1 = new Group("TEST_1");
        Group group_2 = new Group("TEST_2");
        group_1.addMember(employee);
        group_1.addMember(employee2);
        group_2.addMember(employee);
        directory.addTeam(employee);
        directory.addTeam(new Group("group_1"));
        directory.addTeam(new Group("group_2"));
        directory.addTeam(group_1);
        directory.addTeam(group_2);
        System.out.println("Start");
        for(Team team : directory) {
            team.printName();
        }
    }

    /**
     * Test for Employee, creating, finding and deleting in derby
     */
    @Test
    public void testEmployeeDB(){
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        Employee employee2 = new Employee.Builder("Hendrix", "Jimmy", "GuitarHero").updatePhoneList(tmp).build();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.create(employee);
        employeeDAO.create(employee2);
        Employee result1 = employeeDAO.find("Dylan");
        Employee result2 = employeeDAO.find("Hendrix");
        employeeDAO.delete("Dylan");
        employeeDAO.delete("Hendrix");
        assertEquals(result1.getName(),employee.getName());
        assertEquals(result1.getSurname(),employee.getSurname());
        assertEquals(result1.getFunction(), employee.getFunction());
        //PHONE
        assertEquals(result1.getBirthDate(),employee.getBirthDate());

        assertEquals(result2.getName(), employee2.getName());
        assertEquals(result2.getSurname(), employee2.getSurname());
        assertEquals(result2.getFunction(), employee2.getFunction());
        //PHONE
        assertEquals(result2.getBirthDate(), employee2.getBirthDate());
    }

    /**
     * Test for Group, creating, finding and deleting in derby
     */
    @Test
    public void testGroupDB(){
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        Group group1 = new Group("Test");
        group1.addMember(employee);
        GroupDAO groupDAO = new GroupDAO();
        groupDAO.create(group1);
        Group result1 = groupDAO.find("Dylan");
        groupDAO.delete("Dylan");
    }

    /**
     * Test for Directory, creating, finding and deleting in derby
     */
    @Test
    public void testDirectoryDB(){
        List<String> tmp = new ArrayList<>();
        Employee employee = new Employee.Builder("Dylan", "Bob", "Singer").updatePhoneList(tmp).build();
        Directory directory1 = Directory.getInstance();
        directory1.addTeam(employee);
        DirectoryDAO directoryDAO = new DirectoryDAO();
        directoryDAO.create(directory1);
        Directory result1 = directoryDAO.find("Dylan");
        directoryDAO.delete("Dylan");
    }

}
