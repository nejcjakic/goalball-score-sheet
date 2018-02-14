package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.util.Constants;

public class TournamentGamesTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  // indices of items as located in the table.
  public final static int COLUMN_IDX_GAME_NO = 0;
  public final static int COLUMN_IDX_TEAM_A_NAME = 1;
  public final static int COLUMN_IDX_TEAM_B_NAME = 2;
  public final static int COLUMN_IDX_DATE = 3;
  public final static int COLUMN_IDX_TIME = 4;
  public final static int COLUMN_IDX_REFEREE_1_NAME = 5;
  public final static int COLUMN_IDX_REFEREE_2_NAME = 6;
  public final static int COLUMN_IDX_GAME_SCORE = 7;

  protected String columnNames[] = { "Game no", "Team A", "Team B", "Date",
      "Time", "Referee 1", "Referee 2", "Game score" };

  private List<TournamentGame> allGames;

  public TournamentGamesTableModel(List<TournamentGame> games) {
    Collections.sort(games);
    this.allGames = games;
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public String getColumnName(int col) {
    return columnNames[col];
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == COLUMN_IDX_GAME_NO) {
      return Integer.class;
    }
    return String.class;
  }

  @Override
  public int getRowCount() {
    return allGames.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    TournamentGame game = allGames.get(rowIndex);
    switch (columnIndex) {
    case COLUMN_IDX_GAME_NO:
      return game.getGameNumber();
    case COLUMN_IDX_TEAM_A_NAME:
      return game.getTeamA().getDisplayName();
    case COLUMN_IDX_TEAM_B_NAME:
      return game.getTeamB().getDisplayName();
    case COLUMN_IDX_DATE:
      if (game.getGameDate() != null) {
        return Constants.DEFAULT_DATE_FORMAT.format(game.getGameDate());
      }
      return null;
    case COLUMN_IDX_TIME:
      return game.getGameTime();
    case COLUMN_IDX_REFEREE_1_NAME:
      return game.getReferee1().getFullName();
    case COLUMN_IDX_REFEREE_2_NAME:
      return game.getReferee2().getFullName();
    case COLUMN_IDX_GAME_SCORE:
      if (game.getScoreTeamA() != null && game.getScoreTeamB() != null) {
        return game.getScoreTeamA() + " : " + game.getScoreTeamB();
      }
      return null;
    default:
      return null;
    }
  }

  public void addTournamentGame(TournamentGame tournamentGame) {
    allGames.add(tournamentGame);
    Collections.sort(allGames);
    fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
  }
  
  public void addTournamentGames(List<TournamentGame> tournamentGames) {
    allGames.addAll(tournamentGames);
    Collections.sort(allGames);
    fireTableDataChanged();
  }

  public TournamentGame getTournamentGame(int index) {
    return allGames.get(index);
  }

  public void removeTournamentGame(TournamentGame tournamentGame, int row) {
    allGames.remove(tournamentGame);
    fireTableRowsDeleted(row, row);
  }
}
