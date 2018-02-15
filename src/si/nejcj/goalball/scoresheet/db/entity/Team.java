package si.nejcj.goalball.scoresheet.db.entity;

public class Team extends BasicEntity<Team> implements Comparable<Team> {
  protected String teamName;
  protected String country;
  protected boolean nationalTeam;
  protected boolean male;

  public Team() {
  }

  public Team(Integer id, String teamName, String country, boolean nationalTeam, boolean male) {
    this.id = id;
    this.teamName = teamName;
    this.country = country;
    this.nationalTeam = nationalTeam;
    this.male = male;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public boolean isNationalTeam() {
    return nationalTeam;
  }

  public void setNationalTeam(boolean nationalTeam) {
    this.nationalTeam = nationalTeam;
  }
  
  public boolean isMale() {
    return male;
  }

  public void setMale(boolean isMale) {
    this.male = isMale;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + (male ? 1231 : 1237);
    result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof Team)) {
      return false;
    }
    Team other = (Team) obj;
    if (country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!country.equals(other.country)) {
      return false;
    }
    if (male != other.male) {
      return false;
    }
    if (teamName == null) {
      if (other.teamName != null) {
        return false;
      }
    } else if (!teamName.equals(other.teamName)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean hasEqualData(Team other) {
    if (country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!country.equals(other.country)) {
      return false;
    }
    if (nationalTeam != other.nationalTeam) {
      return false;
    }
    if (teamName == null) {
      if (other.teamName != null) {
        return false;
      }
    } else if (!teamName.equals(other.teamName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("TEAM: ");
    str.append(teamName).append(" nationalTeam: ").append(nationalTeam)
        .append(" ").append(country);
    return str.toString();
  }

  @Override
  public int compareTo(Team other) {
    String team1 = "";
    String team2 = "";
    if (isNationalTeam()) {
      team1 = country;
    } else {
      team1 = teamName + " / " + country;
    }

    if (other.isNationalTeam()) {
      team2 = other.country;
    } else {
      team2 = other.teamName + " / " + other.country;
    }

    return team1.compareTo(team2);
  }
}
