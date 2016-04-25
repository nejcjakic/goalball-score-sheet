package si.nejcj.goalball.scoresheet.db.entity;

public class OfficialLevel extends BasicEntity<OfficialLevel> {

  private String levelName;
  private String levelDescription;

  public OfficialLevel() {
  }

  public OfficialLevel(Integer id, String levelName, String levelDescription) {
    this.id = id;
    this.levelName = levelName;
    this.levelDescription = levelDescription;
  }

  public String getLevelName() {
    return levelName;
  }

  public void setLevelName(String levelName) {
    this.levelName = levelName;
  }

  public String getLevelDescription() {
    return levelDescription;
  }

  public void setLevelDescription(String levelDescription) {
    this.levelDescription = levelDescription;
  }

  @Override
  public boolean hasEqualData(OfficialLevel other) {
    if (levelDescription == null) {
      if (other.levelDescription != null) {
        return false;
      }
    } else if (!levelDescription.equals(other.levelDescription)) {
      return false;
    }
    if (levelName == null) {
      if (other.levelName != null) {
        return false;
      }
    } else if (!levelName.equals(other.levelName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("OFFICIAL LEVEL: ");
    str.append(levelName).append(" ").append(levelDescription);
    return str.toString();
  }

}
