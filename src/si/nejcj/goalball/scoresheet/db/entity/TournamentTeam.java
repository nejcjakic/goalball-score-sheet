package si.nejcj.goalball.scoresheet.db.entity;

public class TournamentTeam extends Team {

  public TournamentTeam() {
    super();
  }

  public TournamentTeam(Integer id, String teamName, String country,
      boolean nationalTeam, boolean isMale) {
    super(id, teamName, country, nationalTeam, isMale);
  }

  public String getSimpleName() {
    if (isNationalTeam()) {
      return country;
    }
    return teamName;
  }

  public String getDisplayName() {
    if (nationalTeam) {
      return country;
    }

    return teamName + " / " + country;
  }
}
