package org.uvsq21400579;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectoryDAO extends DAO<Directory> {

  private DAO dao;

  DirectoryDAO(){

  }

  @Override
  public void create(Directory object) {
    String insertFromNameString = "INSERT INTO DIRECTORY(NAME) VALUES(?)";
    String insertFromGroupString = "INSERT INTO DIRECTORY(GROUPNAME) VALUES(?)";
    this.connect();

    try(
        PreparedStatement insertFromGroup = connect.prepareStatement(insertFromGroupString);
        PreparedStatement insertFromName = connect.prepareStatement(insertFromNameString);
        ){
      for(Team t : object){
        if(t instanceof Group){
          this.dao.create(t);
          insertFromGroup.setString(1,t.getName());
          insertFromGroup.executeUpdate();
        }
        else if(t instanceof Employee){
          this.dao.create(t);
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
    String selectFromGroupString = "SELECT * FROM GROUPDIRECTORY D";
    String selectFromNameString = "SELECT * FROM DIRECTORYD";
    this.connect();
    try(
        PreparedStatement selectFromGroup = connect.prepareStatement(selectFromGroupString);
        PreparedStatement selectFromName = connect.prepareStatement(selectFromNameString);
        ) {
      ResultSet resultSetGroup = selectFromGroup.executeQuery();
      while(resultSetGroup.next()){
        directory.addTeam((Group) this.dao.find(resultSetGroup.getString("groupName")));
      }
      ResultSet resultSetName = selectFromName.executeQuery();
      while (resultSetName.next()){
        directory.addTeam((Employee) this.dao.find(resultSetName.getString("name")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    this.disconnect();
    return directory;
  }

  @Override
  public void delete(String key) {
    String deleteFromGroupString = "SELECT FROM GROUPDIRECTORY D WHERE D.groupname = ?";
    String deleteFromNameString = "SELECT FROM DIRECTORY D WHERE D.name = ?";
    this.connect();
    try(
        PreparedStatement deleteFromGroup = connect.prepareStatement(deleteFromGroupString);
        PreparedStatement deleteFromName = connect.prepareStatement(deleteFromNameString);
        ){
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
    super.connect.close();
  }
}
