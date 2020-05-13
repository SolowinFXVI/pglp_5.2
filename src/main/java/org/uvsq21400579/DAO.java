package org.uvsq21400579;

import java.sql.Connection;

public interface DAO<T> {
  Connection connect = null;

  void create(T object);

  T find(String path);

  void delete(String path);
}
