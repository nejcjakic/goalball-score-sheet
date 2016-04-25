package si.nejcj.goalball.scoresheet.db.entity;

public class TournamentPlayer extends Player {
  private Integer playerNumber;
  private String teamName;

  public TournamentPlayer() {
    super();
  }

  public TournamentPlayer(Integer id, String lastName, String firstName,
      Integer playerNumber, String teamName) {
    super(id, lastName, firstName);
    this.playerNumber = playerNumber;
  }

  public Integer getPlayerNumber() {
    return playerNumber;
  }

  public void setPlayerNumber(Integer playerNumber) {
    this.playerNumber = playerNumber;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("TOURNAMENT_PLAYER: ");
    str.append(lastName).append(" ").append(firstName).append(" ")
        .append(playerNumber).append(" ").append(teamName);
    return str.toString();
  }
}
