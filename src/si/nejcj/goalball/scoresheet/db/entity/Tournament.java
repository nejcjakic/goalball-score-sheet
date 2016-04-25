package si.nejcj.goalball.scoresheet.db.entity;

import java.util.Date;

public class Tournament extends BasicEntity<Tournament> implements
    Comparable<Tournament> {
  private String tournamentName;
  private String location;
  private Date startDate;
  private Date endDate;

  public Tournament() {
  }

  public Tournament(Integer id, String tournamentName, String location,
      Date startDate, Date endDate) {
    this.id = id;
    this.tournamentName = tournamentName;
    this.location = location;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getTournamentName() {
    return tournamentName;
  }

  public void setTournamentName(String tournamentName) {
    this.tournamentName = tournamentName;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean hasEqualData(Tournament other) {
    if (endDate == null) {
      if (other.endDate != null) {
        return false;
      }
    } else if (!endDate.equals(other.endDate)) {
      return false;
    }
    if (location == null) {
      if (other.location != null) {
        return false;
      }
    } else if (!location.equals(other.location)) {
      return false;
    }
    if (startDate == null) {
      if (other.startDate != null) {
        return false;
      }
    } else if (!startDate.equals(other.startDate)) {
      return false;
    }
    if (tournamentName == null) {
      if (other.tournamentName != null) {
        return false;
      }
    } else if (!tournamentName.equals(other.tournamentName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("TOURNAMENT: ");
    str.append(tournamentName).append(", ").append(location)
        .append(" startDate: ").append(startDate).append(" endDate: ")
        .append(endDate);
    return str.toString();
  }

  @Override
  public int compareTo(Tournament other) {
    return tournamentName.compareTo(other.getTournamentName());
  }

}
