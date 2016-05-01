package si.nejcj.goalball.scoresheet.db.entity.util;

public class RefereeGame implements Comparable<RefereeGame> {
  private Integer gameNo;
  private String gameTime;
  private String position;

  public RefereeGame(Integer gameNo, String gameTime, String position) {
    super();
    this.gameNo = gameNo;
    this.gameTime = gameTime;
    this.position = position;
  }

  public Integer getGameNo() {
    return gameNo;
  }

  public void setGameNo(Integer gameNo) {
    this.gameNo = gameNo;
  }

  public String getGameTime() {
    return gameTime;
  }

  public void setGameTime(String gameTime) {
    this.gameTime = gameTime;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Override
  public int compareTo(RefereeGame o) {
    return gameNo.compareTo(o.gameNo);
  }
}
