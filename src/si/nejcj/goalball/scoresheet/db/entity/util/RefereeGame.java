package si.nejcj.goalball.scoresheet.db.entity.util;

import java.util.Date;

public class RefereeGame implements Comparable<RefereeGame> {
  private Integer gameNo;
  private Date gameDate;
  private String gameTime;
  private String venue;
  private String position;

  public RefereeGame(Integer gameNo, Date gameDate, String gameTime,
      String venue, String position) {
    super();
    this.gameNo = gameNo;
    this.gameDate = gameDate;
    this.gameTime = gameTime;
    this.venue = venue;
    this.position = position;
  }

  public Integer getGameNo() {
    return gameNo;
  }

  public void setGameNo(Integer gameNo) {
    this.gameNo = gameNo;
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

  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
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
