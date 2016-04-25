package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.TournamentPlayer;

public class TournamentPlayersTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  // indices of items as located in the table.
  public final static int COLUMN_IDX_PARTICIPATING = 0;
  public final static int COLUMN_PLAYER_NUMBER = 1;
  public final static int COLUMN_IDX_LAST_NAME = 2;
  public final static int COLUMN_IDX_FIRST_NAME = 3;

  protected String columnNames[] = { "Participating", "Player number",
      "Last name", "First name" };

  private List<TournamentPlayer> allPlayers;
  private List<TournamentPlayer> selectedPlayers;

  public TournamentPlayersTableModel(List<TournamentPlayer> players) {
    this.allPlayers = players;
    this.selectedPlayers = new ArrayList<TournamentPlayer>();
  }

  public TournamentPlayersTableModel(List<TournamentPlayer> players,
      List<TournamentPlayer> selectedPlayers) {
    this.allPlayers = players;
    this.selectedPlayers = selectedPlayers;
    for (TournamentPlayer tournamentPlayer : selectedPlayers) {
      for (TournamentPlayer player : allPlayers) {
        if (tournamentPlayer.hasEqualData(player)) {
          tournamentPlayer.setId(player.getId());
          allPlayers.remove(player);
          allPlayers.add(tournamentPlayer);
          break;
        }
      }
    }
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
    switch (columnIndex) {
    case COLUMN_IDX_PARTICIPATING:
      return Boolean.class;
    case COLUMN_PLAYER_NUMBER:
      return Integer.class;
    default:
      return String.class;
    }
  }

  @Override
  public int getRowCount() {
    return allPlayers.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if (col == COLUMN_IDX_PARTICIPATING || col == COLUMN_PLAYER_NUMBER) {
      return true;
    }
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    TournamentPlayer player = allPlayers.get(rowIndex);
    switch (columnIndex) {
    case COLUMN_IDX_PARTICIPATING:
      return selectedPlayers.contains(player);
    case COLUMN_PLAYER_NUMBER:
      return player.getPlayerNumber();
    case COLUMN_IDX_LAST_NAME:
      return player.getLastName();
    case COLUMN_IDX_FIRST_NAME:
      return player.getFirstName();
    default:
      return null;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    TournamentPlayer player = allPlayers.get(row);
    switch (column) {
    case COLUMN_IDX_PARTICIPATING:
      if (value instanceof Boolean) {
        boolean selected = (Boolean) value;
        if (selected) {
          selectedPlayers.add(player);
        } else {
          selectedPlayers.remove(player);
        }
      }
      break;
    case COLUMN_PLAYER_NUMBER:
      if (value instanceof Integer) {
        Integer playerNumber = (Integer) value;
        player.setPlayerNumber(playerNumber);
      }
      break;
    }
    fireTableCellUpdated(row, column);
  }

  public List<TournamentPlayer> getSelectedPlayers() {
    return selectedPlayers;
  }

}
