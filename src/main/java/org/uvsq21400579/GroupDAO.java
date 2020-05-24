package org.uvsq21400579;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class GroupDAO extends DAO<Group> {

  private final DAO dao = new EmployeeDAO();

  @Override
  public void create(Group object) {

    String insertGroupString = "INSERT INTO \"GROUP\"(name) VALUES(?)";
    String insertGroupMembersString = "INSERT INTO GROUPMEMBERS(GROUPNAME , EMPLOYEENAME) VALUES (?,?) ";

    this.connect();

    try (
        PreparedStatement insertGroup = this.connection.prepareStatement(insertGroupString);
        PreparedStatement insertGroupMembers = this.connection.prepareStatement(insertGroupMembersString)
    ) {
      this.statement = connection.createStatement();
      insertGroup.setString(1, object.getName());
      insertGroup.executeUpdate();

      for (Team t : object) {
        if (t instanceof Iterable && !object.getName().equals(t.getName())) {
          this.dao.create(t);
          insertGroup.setString(1,object.getName());
          insertGroup.executeUpdate();
        } else if (t instanceof Employee) {
          this.dao.create(t);
          insertGroupMembers.setString(1, t.getName());
          insertGroupMembers.setString(2, t.getName());
          insertGroupMembers.executeUpdate();
        }
      }
    } catch (SQLIntegrityConstraintViolationException e) {
      System.out.println("Shape already exists ignoring");
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    this.disconnect();
  }

  @Override
  public Group find(String key) {
    Group group = null;
    String selectAllString = "SELECT * FROM \"GROUP\" WHERE \"GROUP\".NAME = ?";
    String selectGroupMembers = "SELECT * FROM GROUPMEMBERS GM WHERE GM.GROUPNAME = ?";
    this.connect();
    try (
        PreparedStatement selectAll = this.connection.prepareStatement(selectAllString);
        PreparedStatement selectMembers = this.connection.prepareStatement(selectGroupMembers)
    ) {
      selectAll.setString(1,key);
      ResultSet resultSetAll = selectAll.executeQuery();

      while (resultSetAll.next()) {
        group = new Group(resultSetAll.getString("name"));

        selectMembers.setString(1,key);
        ResultSet resultSetEmployee = selectMembers.executeQuery();
        while (resultSetEmployee.next()) {
          group.addMember((Employee) this.dao.find(resultSetEmployee.getString("name")));
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
    String deleteString = "DELETE FROM \"GROUP\" G WHERE G.name = ?";

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
