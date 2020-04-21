package org.uvsq21400579;

import java.io.Serializable;

public class Node<T> implements Serializable {

  private Node<T> next = null;
  private final T element;

  public Node(T element) {
    this.element = element;
  }

  /**
   * Add Element to Node.
   * @param next Next node.
   */
  public void addElement(T next) {
    if (this.next == null) {
      this.next = new Node<>(next);
    } else {
      this.next.addElement(next);
    }
  }

  public T getElement() {
    return this.element;
  }

  public boolean hasNext() {
    return this.next != null;
  }

  public Node<T> getNext() {
    return this.next;
  }
}
