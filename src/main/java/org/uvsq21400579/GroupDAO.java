package org.uvsq21400579;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO extends DAO<Group> {

  private DAO dao;

  public GroupDAO() {
  }


  @Override
  public void create(Group object) {

    String insertTeamString = "INSERT INTO isTeam(groupName, name) VALUES(?,?)"; //TODO
    String insertEmployeeString = "INSERT INTO Group(name) VALUES(?)";
    String insertGroupString = "INSERT INTO isGroup(groupName, name) VALUES(?,?)"; //TODO

    this.connect();

    try(
        PreparedStatement insertTeam = this.connect.prepareStatement(insertTeamString);
        PreparedStatement insertEmployee = this.connect.prepareStatement(insertEmployeeString);
        PreparedStatement insertGroup = this.connect.prepareStatement(insertGroupString)

    ){
      this.statement = connect.createStatement();
      insertEmployee.setString(1, object.getName());
      insertEmployee.executeUpdate();

      for (Team t : object){
        if(t instanceof Iterable && !object.getName().equals(t.getName())){
          this.dao.create(t);
          insertGroup.setString(1,object.getName());
          insertGroup.setString(1,object.getName());
          insertGroup.executeUpdate();
        }
        else if(t instanceof Employee){
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

    String selectGroupString = "SELECT IG.name FROM IsGroup IG WHERE IG.groupName = ?";
    String selectAllString = "SELECT * FROM Group G WHERE G.name = ?";
    String selectEmployeeString = "SELECT IE FROM IsEmployee IE WHERE IE.groupName = ?";

    this.connect();
    try(
        PreparedStatement selectGroup = this.connect.prepareStatement(selectGroupString);
        PreparedStatement selectAll = this.connect.prepareStatement(selectAllString);
        PreparedStatement selectEmployee = this.connect.prepareStatement(selectEmployeeString)
    ) {
      selectAll.setString(1,key);
      ResultSet resultSetAll = selectAll.executeQuery();

      while (resultSetAll.next()){
        group = new Group(resultSetAll.getString("name"));

        selectEmployee.setString(1,key);
        ResultSet resultSetEmployee = selectEmployee.executeQuery();
        while (resultSetEmployee.next()){
          group.addMember((Employee) this.dao.find(resultSetEmployee.getString("name")));
        }

        selectGroup.setString(1, key);
        ResultSet resultSetGroup = selectGroup.executeQuery();
        while (resultSetGroup.next()){
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
    String deleteString = "SELECT FROM GROUP G WHERE G.name = ?";

    this.connect();
    try(
        PreparedStatement delete = this.connect.prepareStatement(deleteString)
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
    super.connect.close();
  }
}
