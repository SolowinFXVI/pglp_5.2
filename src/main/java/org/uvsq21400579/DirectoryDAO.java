package org.uvsq21400579;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DirectoryDAO implements DAO<Directory> {

  @Override
  public Directory create(Directory directory) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(
        new BufferedOutputStream(
            new FileOutputStream("directory")))) {
      outputStream.writeObject(directory);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return directory;
  }

  @Override
  public Directory find(String path) {
    Directory directory = null;
    try (ObjectInputStream objectInputStream = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream(path)))) {
      directory = (Directory) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException exception) {
      exception.printStackTrace();
    }
    return directory;
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
