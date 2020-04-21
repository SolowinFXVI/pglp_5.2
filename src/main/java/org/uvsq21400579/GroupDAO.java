package org.uvsq21400579;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GroupDAO implements DAO<Group> {

  @Override
  public Group create(Group object) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(
        new BufferedOutputStream(
            new FileOutputStream("group")))) {
      outputStream.writeObject(object);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return object;
  }

  @Override
  public Group find(String path) {
    Group group = null;
    try (ObjectInputStream objectInputStream = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream(path)))) {
      group = (Group) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException exception) {
      exception.printStackTrace();
    }
    return group;
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
