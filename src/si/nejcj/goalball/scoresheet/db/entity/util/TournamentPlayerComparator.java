package si.nejcj.goalball.scoresheet.db.entity.util;

import java.util.Comparator;

import si.nejcj.goalball.scoresheet.db.entity.TournamentPlayer;

public class TournamentPlayerComparator implements Comparator<TournamentPlayer> {

  @Override
  public int compare(TournamentPlayer player1, TournamentPlayer player2) {
    if (player1.getPlayerNumber() == null || player1.getPlayerNumber() == 0) {
      return -1;
    }
    if (player2.getPlayerNumber() == null || player2.getPlayerNumber() == 0) {
      return 1;
    }
    return player1.getPlayerNumber().compareTo(player2.getPlayerNumber());
  }

}
