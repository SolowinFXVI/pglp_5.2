package org.uvsq21400579;

import java.io.Serializable;
import java.util.Iterator;

public class Directory implements Iterable<Team>, Serializable {

  private static Directory Directory;
  private final TeamIterator<Team> head;

  private Directory() {
    head = new TeamIterator<>();
  }

  /**
   * Instance of Directory.
   * @return Directory
   */
  public static Directory getInstance() {
    if (Directory == null) {
      Directory = new Directory();
    }
    return Directory;
  }

  @Override
  public Iterator<Team> iterator() {
    return this.head;
  }


  public void addTeam(Team e) {
    this.head.add(e);
  }
}
