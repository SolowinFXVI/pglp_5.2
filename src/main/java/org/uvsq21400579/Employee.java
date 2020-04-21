package org.uvsq21400579;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class Employee extends Team implements Serializable {
  private final String name;
  private final String surname;
  private final String function;
  private final List<String> phone;
  private final java.time.LocalDate birthDate;

  public static class Builder {
    private final String name;
    private final String surname;
    private final String function;
    private  List<String> phone = null;
    private  java.time.LocalDate birthDate = java.time.LocalDate.now();

    /**
     * Builder for Employee.
     * @param name Name of employee.
     * @param surname Surname of employee.
     * @param function Function of employee.
     */
    public Builder(String name, String surname, String function) {
      this.name = name;
      this.surname = surname;
      this.function = function;
    }

    public Builder updatePhoneList(List<String> phone) {
      this.phone = phone;
      return this;
    }

    public Builder updateBirthDate(java.time.LocalDate t) {
      this.birthDate = t;
      return this;
    }

    public Employee build() {
      return new Employee(this);
    }

  }

  private Employee(Builder build) {
    this.name = build.name;
    this.surname = build.surname;
    this.function = build.function;
    this.phone = build.phone;
    this.birthDate = build.birthDate;
  }

  public String getName() {
    return this.name;
  }

  public List<String> getPhone() {
    return Collections.unmodifiableList(this.phone);
  }

  public java.time.LocalDate getLocalDate() {
    return this.birthDate;
  }

  public void printName() {
    System.out.println(this.name);
  }

}
