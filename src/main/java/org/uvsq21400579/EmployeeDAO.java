package org.uvsq21400579;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EmployeeDAO implements DAO<Employee> {

  @Override
  public Employee create(Employee object) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(
        new FileOutputStream("employee")))) {
      outputStream.writeObject(object);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return object;
  }

  @Override
  public Employee find(String path) {
    Employee employee = null;
    try (ObjectInputStream objectInputStream = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream(path)))) {
      employee = (Employee) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException exception) {
      exception.printStackTrace();
    }
    return employee;
  }

  @Override
  public void delete(String path) {
    try {
      File file = new File(path);
      file.delete();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
