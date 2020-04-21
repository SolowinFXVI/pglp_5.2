package org.uvsq21400579;

import java.sql.Connection;

public interface DAO<T> {
  Connection connect = null;

  public T create(T object);

  public T find(String path);

  public void delete(String path);
}
