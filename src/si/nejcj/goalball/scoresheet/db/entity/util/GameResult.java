package si.nejcj.goalball.scoresheet.db.entity.util;

public class GameResult implements Comparable<GameResult> {

  public static final String GENERAL_GAME = "generalGame";

  private String teamName;
  private int points = 0;
  private int wins = 0;
  private int draws = 0;
  private int losses = 0;
  private int goalsScored = 0;
  private int goalsConceded = 0;

  public GameResult(String teamName) {
    this.teamName = teamName;
  }

  public String getTeamName() {
    return teamName;
  }

  public int getPoints() {
    return points;
  }

  public int getWins() {
    return wins;
  }

  public int getDraws() {
    return draws;
  }

  public int getLosses() {
    return losses;
  }

  public int getGoalsScored() {
    return goalsScored;
  }

  public int getGoalsConceded() {
    return goalsConceded;
  }

  public int getGoalDifference() {
    return goalsScored - goalsConceded;
  }

  public void addWin() {
    points += 3;
    wins++;
  }

  public void addDraw() {
    points++;
    draws++;
  }

  public void addLoss() {
    losses++;
  }

  public void addGoalsScored(int goalsScored) {
    this.goalsScored += goalsScored;
  }

  public void addGoalsConceded(int goalsConceded) {
    this.goalsConceded += goalsConceded;
  }

  @Override
  public int compareTo(GameResult other) {
    // 1) Points
    if (getPoints() > other.getPoints()) {
      return -1;
    } else if (getPoints() < other.getPoints()) {
      return 1;
    }
    // 2) Goal difference
    if (getGoalDifference() > other.getGoalDifference()) {
      return -1;
    } else if (getGoalDifference() < other.getGoalDifference()) {
      return 1;
    }
    // 3) Number of wins
    if (getWins() > other.getWins()) {
      return -1;
    } else if (getWins() < other.getWins()) {
      return 1;
    }
    // 4) Goals scored
    if (getGoalsScored() > other.getGoalsScored()) {
      return -1;
    } else if (getGoalsScored() < other.getGoalsScored()) {
      return 1;
    }
    // 5) Difference at half time
    // TODO: Not yet implemented
    return 0;
  }
}
