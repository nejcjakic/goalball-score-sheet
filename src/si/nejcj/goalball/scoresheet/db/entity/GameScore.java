package si.nejcj.goalball.scoresheet.db.entity;

public class GameScore extends BasicEntity<GameScore> {
  private Integer gameId;
  private Integer playerId;
  private Integer noOfGoals;

  public GameScore() {
  }

  public GameScore(Integer id, Integer gameId, Integer playerId,
      Integer noOfGoals) {
    super();
    this.id = id;
    this.gameId = gameId;
    this.playerId = playerId;
    this.noOfGoals = noOfGoals;
  }

  public Integer getGameId() {
    return gameId;
  }

  public void setGameId(Integer gameId) {
    this.gameId = gameId;
  }

  public Integer getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Integer playerId) {
    this.playerId = playerId;
  }

  public Integer getNoOfGoals() {
    return noOfGoals;
  }

  public void setNoOfGoals(Integer noOfGoals) {
    this.noOfGoals = noOfGoals;
  }

  @Override
  public boolean hasEqualData(GameScore other) {
    return gameId.equals(other.gameId) && playerId.equals(other.playerId);
  }
}
