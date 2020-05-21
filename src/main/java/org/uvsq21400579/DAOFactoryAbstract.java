package org.uvsq21400579;

public interface DAOFactoryAbstract {
  DAO<Employee> createEmployeeDAO();

  DAO<Group> createGroupDAO();

  DAO<Directory> createDirectoryDAO();
}
