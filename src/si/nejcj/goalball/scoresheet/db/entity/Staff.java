package si.nejcj.goalball.scoresheet.db.entity;

public class Staff extends BasicEntity<Staff> {
  protected String lastName;
  protected String firstName;
  protected String personPosition;

  public Staff() {
  }

  public Staff(Integer id, String lastName, String firstName,
      String personPosition) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.personPosition = personPosition;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getPersonPosition() {
    return personPosition;
  }

  public void setPersonPosition(String personPosition) {
    this.personPosition = personPosition;
  }

  @Override
  public boolean hasEqualData(Staff other) {
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!lastName.equals(other.lastName)) {
      return false;
    }
    if (personPosition == null) {
      if (other.personPosition != null) {
        return false;
      }
    } else if (!personPosition.equals(other.personPosition)) {
      return false;
    }
    return true;
  }
  
  public boolean isEqualPerson(Staff other) {
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!lastName.equals(other.lastName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("STAFF: ");
    str.append(lastName).append(" ").append(firstName).append(" ")
        .append(personPosition);
    return str.toString();
  }
}
