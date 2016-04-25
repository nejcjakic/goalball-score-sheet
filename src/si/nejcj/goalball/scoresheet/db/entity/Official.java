package si.nejcj.goalball.scoresheet.db.entity;

public class Official extends BasicEntity<Official> implements
    Comparable<Official> {
  protected String lastName;
  protected String firstName;
  protected String gender;
  protected String country;
  protected OfficialLevel officialLevel;

  public Official() {
    this.officialLevel = new OfficialLevel(1, "ITO",
        "International Technical Official");
  }

  public Official(Integer id, String lastName, String firstName, String gender,
      String country, OfficialLevel officialLevel) {
    super();
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.gender = gender;
    this.country = country;
    this.officialLevel = officialLevel;
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

  public OfficialLevel getOfficialLevel() {
    return officialLevel;
  }

  public void setOfficialLevel(OfficialLevel officialLevel) {
    this.officialLevel = officialLevel;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getFullName() {
    return lastName + " " + firstName;
  }

  @Override
  public boolean hasEqualData(Official other) {
    if (country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!country.equals(other.country)) {
      return false;
    }
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }
    if (gender == null) {
      if (other.gender != null) {
        return false;
      }
    } else if (!gender.equals(other.gender)) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!lastName.equals(other.lastName)) {
      return false;
    }
    if (officialLevel == null) {
      if (other.officialLevel != null) {
        return false;
      }
    } else if (!officialLevel.hasEqualData(other.officialLevel)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("OFFICIAL: ");
    str.append(id).append(" ").append(lastName).append(" ").append(firstName)
        .append(" ").append(gender).append(" ").append(country).append(" ")
        .append(officialLevel);
    return str.toString();
  }

  @Override
  public int compareTo(Official other) {
    if (lastName == null) {
      return -1;
    }
    if (lastName.equals(other.lastName)) {
      return firstName.compareTo(other.firstName);
    }
    return lastName.compareTo(other.lastName);
  }
}
