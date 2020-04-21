package org.uvsq21400579;

import java.io.Serializable;
import java.util.Iterator;

public class Group extends Team implements Iterable<Team>, Serializable {

  private final String nom;
  private final TeamIterator<Team> head;

  /**
   * Group Constructor.
   * @param name Name of Group.
   */
  public Group(String name) {
    this.nom = name;
    this.head = new TeamIterator<>();
  }

  public void addMember(Team e) {
    this.head.add(e);
  }

  /**
   * Prints the name of the Group.
   */
  public void printName() {
    System.out.println(this.nom);
    for (Team e : this) {
      e.printName();
    }
  }

  @Override
  public Iterator<Team> iterator() {
    return this.head;
  }

}