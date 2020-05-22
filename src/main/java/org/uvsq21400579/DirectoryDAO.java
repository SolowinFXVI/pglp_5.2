package org.uvsq21400579;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectoryDAO extends DAO<Directory> {

  private final DAO employeeDAO = new EmployeeDAO();
  private final DAO groupDAO = new GroupDAO();

  @Override
  public void create(Directory object) {
    String insertFromNameString = "INSERT INTO DIRECTORY(NAME) VALUES(?)";
    String insertFromGroupString = "INSERT INTO GROUPDIRECTORY(NAME) VALUES(?)";
    this.connect();

    try (
        PreparedStatement insertFromGroup = connection.prepareStatement(insertFromGroupString);
        PreparedStatement insertFromName = connection.prepareStatement(insertFromNameString)
        ) {
      for (Team t : object) {
        if (t instanceof Group) {
          this.groupDAO.create(t);
          insertFromGroup.setString(1,t.getName());
          insertFromGroup.executeUpdate();
        } else if (t instanceof Employee) {
          this.employeeDAO.create(t);
          insertFromName.setString(1,t.getName());
          insertFromName.executeUpdate();
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    this.disconnect();
  }

  @Override
  public Directory find(String key) {
    Directory directory = null;
    String selectFromGroupString = "SELECT * FROM GROUPDIRECTORY";
    String selectFromNameString = "SELECT * FROM DIRECTORY";
    this.connect();
    try (
        PreparedStatement selectFromGroup = connection.prepareStatement(selectFromGroupString);
        PreparedStatement selectFromName = connection.prepareStatement(selectFromNameString)
        ) {
      ResultSet resultSetGroup = selectFromGroup.executeQuery();
      while (resultSetGroup.next()) {
        if (directory != null) {
          directory.addTeam((Group) this.groupDAO.find(resultSetGroup.getString("NAME")));
        }
      }
      ResultSet resultSetName = selectFromName.executeQuery();
      while (resultSetName.next()) {
        if (directory != null) {
          directory.addTeam((Employee) this.employeeDAO.find(resultSetName.getString("NAME")));
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    this.disconnect();
    return directory;
  }

  @Override
  public void delete(String key) {
    String deleteFromGroupString = "SELECT FROM GROUPDIRECTORY D WHERE D.NAME = ?";
    String deleteFromNameString = "SELECT FROM DIRECTORY D WHERE D.NAME = ?";
    this.connect();
    try (
        PreparedStatement deleteFromGroup = connection.prepareStatement(deleteFromGroupString);
        PreparedStatement deleteFromName = connection.prepareStatement(deleteFromNameString)
        ) {
      deleteFromGroup.setString(1,key);
      deleteFromGroup.executeUpdate();
      deleteFromName.setString(1,key);
      deleteFromGroup.executeUpdate();
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
