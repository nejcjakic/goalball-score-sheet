package si.nejcj.goalball.scoresheet.db.entity;

public class Player extends BasicEntity<Player> {
  protected String lastName;
  protected String firstName;
  protected boolean isMale;

  public Player() {
  }

  public Player(Integer id, String lastName, String firstName, boolean isMale) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.isMale = isMale;
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

  public boolean isMale() {
    return isMale;
  }

  public void setMale(boolean isMale) {
    this.isMale = isMale;
  }

  public String getFullName() {
    return lastName + " " + firstName;
  }

  @Override
  public boolean hasEqualData(Player other) {
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
    StringBuilder str = new StringBuilder("PLAYER: ");
    str.append(lastName).append(" ").append(firstName);
    return str.toString();
  }
}
