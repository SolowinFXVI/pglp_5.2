package org.uvsq21400579;

public class DAOFactory implements DAOFactoryAbstract{
  @Override
  public DAO<Employee> createEmployeeDAO() {
    return new EmployeeDAO();
  }

  @Override
  public DAO<Group> createGroupDAO() {
    return new GroupDAO();
  }

  @Override
  public DAO<Directory> createDirectoryDAO() {
    return new DirectoryDAO();
  }
}
