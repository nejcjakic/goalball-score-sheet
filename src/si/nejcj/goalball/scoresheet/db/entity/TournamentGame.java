package si.nejcj.goalball.scoresheet.db.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class TournamentGame extends BasicEntity<TournamentGame> implements
    Comparable<TournamentGame> {
  private Tournament tournament;
  private Integer gameNumber;
  private Date gameDate;
  private String gameTime;
  private String pool;
  private String venue;
  private String gender;
  private boolean needsWinner;
  private TournamentTeam tournamentTeamA;
  private TournamentTeam tournamentTeamB;
  private TournamentOfficial referee1;
  private TournamentOfficial referee2;
  private TournamentOfficial tenSeconds1;
  private TournamentOfficial tenSeconds2;
  private TournamentOfficial scorer;
  private TournamentOfficial timer;
  private TournamentOfficial backupTimer;
  private TournamentOfficial goalJudge1;
  private TournamentOfficial goalJudge2;
  private TournamentOfficial goalJudge3;
  private TournamentOfficial goalJudge4;
  private Integer scoreTeamA;
  private Integer scoreTeamB;

  public TournamentGame() {
  }

  public TournamentGame(Integer id, Tournament tournament, Integer gameNumber,
      Date gameDate, String gameTime, String pool, String venue, String gender,
      boolean needsWinner, TournamentTeam tournamentTeamA,
      TournamentTeam tournamentTeamB, TournamentOfficial referee1,
      TournamentOfficial referee2, TournamentOfficial tenSeconds1,
      TournamentOfficial tenSeconds2, TournamentOfficial scorer,
      TournamentOfficial timer, TournamentOfficial backupTimer,
      TournamentOfficial goalJudge1, TournamentOfficial goalJudge2,
      TournamentOfficial goalJudge3, TournamentOfficial goalJudge4,
      Integer scoreTeamA, Integer scoreTeamB) {
    this.id = id;
    this.tournament = tournament;
    this.gameNumber = gameNumber;
    this.gameDate = gameDate;
    this.gameTime = gameTime;
    this.pool = pool;
    this.venue = venue;
    this.gender = gender;
    this.needsWinner = needsWinner;
    this.tournamentTeamA = tournamentTeamA;
    this.tournamentTeamB = tournamentTeamB;
    this.referee1 = referee1;
    this.referee2 = referee2;
    this.tenSeconds1 = tenSeconds1;
    this.tenSeconds2 = tenSeconds2;
    this.scorer = scorer;
    this.timer = timer;
    this.backupTimer = backupTimer;
    this.goalJudge1 = goalJudge1;
    this.goalJudge2 = goalJudge2;
    this.goalJudge3 = goalJudge3;
    this.goalJudge4 = goalJudge4;
    this.scoreTeamA = scoreTeamA;
    this.scoreTeamB = scoreTeamB;
  }

  public Tournament getTournament() {
    return tournament;
  }

  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }

  public Integer getGameNumber() {
    return gameNumber;
  }

  public void setGameNumber(Integer gameNumber) {
    this.gameNumber = gameNumber;
  }

  public Date getGameDate() {
    return gameDate;
  }

  public void setGameDate(Date gameDate) {
    this.gameDate = gameDate;
  }

  public String getGameTime() {
    return gameTime;
  }

  public void setGameTime(String gameTime) {
    this.gameTime = gameTime;
  }

  public String getPool() {
    return pool;
  }

  public void setPool(String pool) {
    this.pool = pool;
  }

  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public boolean getNeedsWinner() {
    return needsWinner;
  }

  public void setNeedsWinner(boolean needsWinner) {
    this.needsWinner = needsWinner;
  }

  public TournamentTeam getTeamA() {
    return tournamentTeamA;
  }

  public void setTeamA(TournamentTeam tournamentTeamA) {
    this.tournamentTeamA = tournamentTeamA;
  }

  public TournamentTeam getTeamB() {
    return tournamentTeamB;
  }

  public void setTeamB(TournamentTeam tournamentTeamB) {
    this.tournamentTeamB = tournamentTeamB;
  }

  public TournamentOfficial getReferee1() {
    return referee1;
  }

  public void setReferee1(TournamentOfficial referee1) {
    this.referee1 = referee1;
  }

  public TournamentOfficial getReferee2() {
    return referee2;
  }

  public void setReferee2(TournamentOfficial referee2) {
    this.referee2 = referee2;
  }

  public TournamentOfficial getTenSeconds1() {
    return tenSeconds1;
  }

  public void setTenSeconds1(TournamentOfficial tenSeconds1) {
    this.tenSeconds1 = tenSeconds1;
  }

  public TournamentOfficial getTenSeconds2() {
    return tenSeconds2;
  }

  public void setTenSeconds2(TournamentOfficial tenSeconds2) {
    this.tenSeconds2 = tenSeconds2;
  }

  public TournamentOfficial getScorer() {
    return scorer;
  }

  public void setScorer(TournamentOfficial scorer) {
    this.scorer = scorer;
  }

  public TournamentOfficial getTimer() {
    return timer;
  }

  public void setTimer(TournamentOfficial timer) {
    this.timer = timer;
  }

  public TournamentOfficial getBackupTimer() {
    return backupTimer;
  }

  public void setBackupTimer(TournamentOfficial backupTimer) {
    this.backupTimer = backupTimer;
  }

  public TournamentOfficial getGoalJudge1() {
    return goalJudge1;
  }

  public void setGoalJudge1(TournamentOfficial goalJudge1) {
    this.goalJudge1 = goalJudge1;
  }

  public TournamentOfficial getGoalJudge2() {
    return goalJudge2;
  }

  public void setGoalJudge2(TournamentOfficial goalJudge2) {
    this.goalJudge2 = goalJudge2;
  }

  public TournamentOfficial getGoalJudge3() {
    return goalJudge3;
  }

  public void setGoalJudge3(TournamentOfficial goalJudge3) {
    this.goalJudge3 = goalJudge3;
  }

  public TournamentOfficial getGoalJudge4() {
    return goalJudge4;
  }

  public void setGoalJudge4(TournamentOfficial goalJudge4) {
    this.goalJudge4 = goalJudge4;
  }

  public boolean hasEqualData(TournamentGame other) {
    return false;
  }

  public Integer getScoreTeamA() {
    return scoreTeamA;
  }

  public void setScoreTeamA(Integer scoreTeamA) {
    this.scoreTeamA = scoreTeamA;
  }

  public Integer getScoreTeamB() {
    return scoreTeamB;
  }

  public void setScoreTeamB(Integer scoreTeamB) {
    this.scoreTeamB = scoreTeamB;
  }

  public String getDefaultScoreSheetFileName() {
    StringBuilder defaultFileName = new StringBuilder();
    defaultFileName.append(getGameNumber()).append("_")
        .append(getTeamA().getSimpleName()).append("_")
        .append(getTeamB().getSimpleName());
    return defaultFileName.toString();
  }

  /**
   * Validates that all data required for valid game has been set. The following
   * attributes are checked: tournament, gameNumber, gameDate, gameTime, gender,
   * tournamentTeamA, tournamentTeamB, referee1, referee2
   * 
   * @return {@code true} if all required attributes are set {@code false}
   *         otherwise
   */
  public boolean hasValidData() {
    if (tournament == null) {
      return false;
    }
    if (gameNumber == null || gameNumber < 1) {
      return false;
    }
    if (gameDate == null) {
      return false;
    }
    if (StringUtils.isBlank(gameTime)) {
      return false;
    }
    if (StringUtils.isBlank(gender)) {
      return false;
    }
    if (tournamentTeamA == null) {
      return false;
    }
    if (tournamentTeamB == null) {
      return false;
    }
    if (referee1 == null) {
      return false;
    }
    if (referee2 == null) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("TOURNAMENT GAME: ");
    str.append(id).append(" ").append(tournament).append(" ")
        .append(gameNumber).append(" ").append(gameDate).append(" ")
        .append(gameTime).append(" ").append(pool).append(" ").append(venue)
        .append(" ").append(gender).append(" ").append(needsWinner).append(" ")
        .append(tournamentTeamA).append(" ").append(tournamentTeamB)
        .append(" ").append(referee1).append(" ").append(referee2).append(" ")
        .append(tenSeconds1).append(" ").append(tenSeconds2).append(" ")
        .append(scorer).append(" ").append(timer).append(" ")
        .append(backupTimer).append(" ").append(goalJudge1).append(" ")
        .append(goalJudge2).append(" ").append(goalJudge3).append(" ")
        .append(goalJudge4);

    return str.toString();
  }

  @Override
  public int compareTo(TournamentGame other) {
    return gameNumber.compareTo(other.gameNumber);
  }
}
