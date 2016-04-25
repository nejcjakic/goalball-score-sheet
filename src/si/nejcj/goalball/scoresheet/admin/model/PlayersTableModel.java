package si.nejcj.goalball.scoresheet.admin.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.Player;

public class PlayersTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -228900779827062446L;

  // indices of items as located in the table.
  public final static int COLUMN_IDX_LAST_NAME = 0;
  public final static int COLUMN_IDX_FIRST_NAME = 1;

  private final String columnNames[] = { "Last name", "First name" };

  private List<Player> players;

  public PlayersTableModel(List<Player> players) {
    this.players = players;
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
  public Class<?> getColumnClass(int c) {
    return String.class;
  }

  @Override
  public int getRowCount() {
    return players.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return true;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Player player = players.get(rowIndex);
    switch (columnIndex) {
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
    Player player = players.get(row);
    switch (column) {
    case COLUMN_IDX_LAST_NAME:
      player.setLastName(value.toString());
      break;
    case COLUMN_IDX_FIRST_NAME:
      player.setFirstName(value.toString());
      break;
    default:
      break;
    }
    fireTableCellUpdated(row, column);
  }

  public Player getPlayer(int index) {
    return players.get(index);
  }

  public void addPlayer(Player player) {
    players.add(player);
    fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
  }

  public void removePlayer(Player player, int row) {
    players.remove(player);
    fireTableRowsDeleted(row, row);
  }
}
