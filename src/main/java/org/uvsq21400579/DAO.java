package org.uvsq21400579;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DAO<T> implements AutoCloseable {
  Connection connect = null;

  public Statement statement = null;

  public void connect(){
    try {
      Class.forName("org.apache.derby.jdbc.EmmbeddedDriver");
      connect = DriverManager.getConnection("jdbc:derby:test;create=true");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  public void disconnect(){
    try{
      connect.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  abstract void create(T object);

  abstract T find(String path);

  abstract void delete(String path);
}
