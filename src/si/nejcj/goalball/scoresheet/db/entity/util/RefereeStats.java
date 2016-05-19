package si.nejcj.goalball.scoresheet.db.entity.util;

import si.nejcj.goalball.scoresheet.db.entity.TournamentOfficial;

public class RefereeStats implements Comparable<RefereeStats> {

  private TournamentOfficial tournamentOfficial;
  private int gamesAsReferee;
  private int gamesAsTableOfficial;
  private int gamesAsGoalJudge;

  public RefereeStats(TournamentOfficial official) {
    this.tournamentOfficial = official;
    gamesAsReferee = 0;
    gamesAsGoalJudge = 0;
    gamesAsTableOfficial = 0;
  }

  public TournamentOfficial getTournamentOfficial() {
    return tournamentOfficial;
  }

  public String getGamesAsReferee() {
    return String.valueOf(gamesAsReferee);
  }

  public String getGamesAsTableOfficial() {
    return String.valueOf(gamesAsTableOfficial);
  }

  public String getGamesAsGoalJudge() {
    return String.valueOf(gamesAsGoalJudge);
  }

  public String getTotalGames() {
    return String
        .valueOf(gamesAsReferee + gamesAsTableOfficial + gamesAsGoalJudge);
  }

  public void increaseGamesAsReferee() {
    gamesAsReferee++;
  }

  public void increaseGamesAsTableOfficial() {
    gamesAsTableOfficial++;
  }

  public void increaseGamesAsGoalJudge() {
    gamesAsGoalJudge++;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((tournamentOfficial == null) ? 0 : tournamentOfficial.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof RefereeStats)) {
      return false;
    }
    RefereeStats other = (RefereeStats) obj;
    if (tournamentOfficial == null) {
      if (other.tournamentOfficial != null) {
        return false;
      }
    } else if (!tournamentOfficial.equals(other.tournamentOfficial)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(RefereeStats o) {
    return tournamentOfficial.getFullName().compareTo(
        o.getTournamentOfficial().getFullName());
  }
}
