package org.uvsq21400579;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO extends DAO<Group> {

  private final DAO dao = new EmployeeDAO();

  @Override
  public void create(Group object) {

    String insertTeamString = "INSERT INTO ISEMPLOYEE(GROUPNAME, NAME) VALUES(?,?)"; //TODO
    String insertEmployeeString = "INSERT INTO \"GROUP\"(name) VALUES(?)";
    String insertGroupString = "INSERT INTO ISGROUP(GROUPNAME, NAME) VALUES(?,?)"; //TODO

    this.connect();

    try (
        PreparedStatement insertTeam = this.connection.prepareStatement(insertTeamString);
        PreparedStatement insertEmployee = this.connection.prepareStatement(insertEmployeeString);
        PreparedStatement insertGroup = this.connection.prepareStatement(insertGroupString)
    ) {
      this.statement = connection.createStatement();
      insertEmployee.setString(1, object.getName());
      insertEmployee.executeUpdate();

      for (Team t : object) {
        if (t instanceof Iterable && !object.getName().equals(t.getName())) {
          this.dao.create(t);
          insertGroup.setString(1,object.getName());
          insertGroup.setString(1,object.getName());
          insertGroup.executeUpdate();
        } else if (t instanceof Employee) {
          this.dao.create(t);
          insertTeam.setString(1, t.getName());
          insertTeam.setString(2, t.getName());
          insertTeam.executeUpdate();
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    this.disconnect();
  }

  @Override
  public Group find(String key) {
    Group group = null;

    String selectGroupString = "SELECT ISGROUP.NAME FROM ISGROUP WHERE ISGROUP.GROUPNAME = ?";
    String selectAllString = "SELECT * FROM \"GROUP\" WHERE \"GROUP\".NAME = ?";
    String selectEmployeeString = "SELECT ISEMPLOYEE.NAME FROM ISEMPLOYEE"
        + " WHERE ISEMPLOYEE.GROUPNAME = ?";

    this.connect();
    try (
        PreparedStatement selectGroup = this.connection.prepareStatement(selectGroupString);
        PreparedStatement selectAll = this.connection.prepareStatement(selectAllString);
        PreparedStatement selectEmployee = this.connection.prepareStatement(selectEmployeeString)
    ) {
      selectAll.setString(1,key);
      ResultSet resultSetAll = selectAll.executeQuery();

      while (resultSetAll.next()) {
        group = new Group(resultSetAll.getString("name"));

        selectEmployee.setString(1,key);
        ResultSet resultSetEmployee = selectEmployee.executeQuery();
        while (resultSetEmployee.next()) {
          group.addMember((Employee) this.dao.find(resultSetEmployee.getString("name")));
        }

        selectGroup.setString(1, key);
        ResultSet resultSetGroup = selectGroup.executeQuery();
        while (resultSetGroup.next()) {
          group.addMember(this.find(resultSetGroup.getString("name")));
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    this.disconnect();

    return group;
  }

  @Override
  public void delete(String key) {
    String deleteString = "SELECT FROM \"GROUP\" G WHERE G.name = ?";

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
