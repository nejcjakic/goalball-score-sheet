package si.nejcj.goalball.scoresheet.db.entity;

public class TournamentOfficial extends Official {

  public TournamentOfficial() {
    super();
  }

  public TournamentOfficial(Integer id, String lastName, String firstName,
      String gender, String country, OfficialLevel officialLevel) {
    super(id, lastName, firstName, gender, country, officialLevel);
  }
}
