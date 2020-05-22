package org.uvsq21400579;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DAO<T> {
  Connection connection;
  public Statement statement;

  public void connect(){
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connection = DriverManager.getConnection("jdbc:derby:derbyDB;create=true");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  public void disconnect() {
    try {
      connection.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  abstract void create(T object);

  abstract T find(String key);

  abstract void delete(String key);

  public abstract void close() throws Exception;
}
