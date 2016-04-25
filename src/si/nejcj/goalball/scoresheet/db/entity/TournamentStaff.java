package si.nejcj.goalball.scoresheet.db.entity;

public class TournamentStaff extends Staff {

  public TournamentStaff() {
    super();
  }

  public TournamentStaff(Integer id, String lastName, String firstName,
      String personPosition) {
    super(id, lastName, firstName, personPosition);
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("TOURNAMENT_STAFF: ");
    str.append(lastName).append(" ").append(firstName).append(" ")
        .append(personPosition);
    return str.toString();
  }
}
