package si.nejcj.goalball.scoresheet.db.entity;

import java.util.Arrays;
import java.util.List;

public class TournamentStaff extends Staff
    implements Comparable<TournamentStaff> {

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

  private final List<String> predefinedOrder = Arrays.asList("Coach",
      "Assistant coach", "Staff");

  @Override
  public int compareTo(TournamentStaff other) {
    return predefinedOrder.indexOf(personPosition)
        - predefinedOrder.indexOf(other.personPosition);
  }
}
