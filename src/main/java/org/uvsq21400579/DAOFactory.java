package org.uvsq21400579;

public class DAOFactory {
  public static DAO<Directory> getDirectoryDAO() {
    return new DirectoryDAO();
  }

  public static DAO<Group> getGroupDAO() {
    return new GroupDAO();
  }

  public static DAO<Employee> getEmployeeDAO() {
    return new EmployeeDAO();
  }
}
