package org.uvsq21400579;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO extends DAO<Employee> {

  @Override
  public void create(Employee object) {
    String insertEmployeeString = "INSERT INTO \"EMPLOYEE\"(NAME, SURNAME,"
        + " \"FUNCTION\", PHONE, BIRTHDATE) values(?, ?, ?, ?, ?)";
    this.connect();
    try (
        PreparedStatement insertEmployee = this.connection.prepareStatement(insertEmployeeString)
        ) {
      insertEmployee.setString(1, object.getName());
      insertEmployee.setString(2, object.getSurname());
      insertEmployee.setString(3,object.getFunction());
      insertEmployee.setString(4, object.getPhone().toString());
      insertEmployee.setString(5, object.getBirthDate().toString());
      insertEmployee.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    this.disconnect();
  }

  @Override
  public Employee find(String key) {
    Employee employee = null;
    String selectString = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.NAME = ?";
    this.connect();
    try (
        PreparedStatement select = this.connection.prepareStatement(selectString)
      ) {
      select.setString(1,key);
      try (ResultSet resultSet = select.executeQuery()) { //TODO add phone
        if (resultSet.next()) {
          employee = new Employee.Builder(resultSet.getString("NAME"),
           resultSet.getString("SURNAME"),
            resultSet.getString("FUNCTION")
        )
          .updateBirthDate(resultSet.getDate("BIRTHDATE").toLocalDate())
          .build();
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    this.disconnect();
    return employee;
  }

  @Override
  public void delete(String key) {
    String deleteString = "DELETE FROM EMPLOYEE E WHERE E.NAME = ?";
    this.connect();
    try (
        PreparedStatement delete = this.connection.prepareStatement(deleteString)
    ) {
      delete.setString(1,key);
      delete.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    this.disconnect();
  }

  @Override
  public void close() throws Exception {
    super.connection.close();
  }
}
