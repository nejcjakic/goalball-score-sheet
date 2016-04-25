package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.Official;
import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;

@SuppressWarnings("serial")
public class TournamentOfficialsTableModel extends AbstractTableModel {

  // indices of items as located in the table.
  public final static int COLUMN_IDX_LAST_NAME = 0;
  public final static int COLUMN_IDX_FIRST_NAME = 1;
  public final static int COLUMN_IDX_OFFICIAL_LEVEL = 2;

  private final String columnNames[] = { "Last name", "First name", "Level" };

  private List<Official> officials;

  public TournamentOfficialsTableModel(List<Official> officials) {
    this.officials = officials;
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
  public Class<?> getColumnClass(int column) {
    switch (column) {
    case COLUMN_IDX_OFFICIAL_LEVEL:
      return OfficialLevel.class;
    default:
      return String.class;
    }
  }

  @Override
  public int getRowCount() {
    return officials.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Official official = officials.get(rowIndex);
    switch (columnIndex) {
    case COLUMN_IDX_LAST_NAME:
      return official.getLastName();
    case COLUMN_IDX_FIRST_NAME:
      return official.getFirstName();
    case COLUMN_IDX_OFFICIAL_LEVEL:
      return official.getOfficialLevel();
    default:
      return null;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    Official official = officials.get(row);
    switch (column) {
    case COLUMN_IDX_LAST_NAME:
      official.setLastName(value.toString());
      break;
    case COLUMN_IDX_FIRST_NAME:
      official.setFirstName(value.toString());
      break;
    case COLUMN_IDX_OFFICIAL_LEVEL:
      if (value instanceof OfficialLevel) {
        official.setOfficialLevel((OfficialLevel) value);
      }
      break;

    default:
      break;
    }
    fireTableCellUpdated(row, column);
  }

  public Official getOfficial(int index) {
    return officials.get(index);
  }

  public void addOfficial(Official official) {
    officials.add(official);
    fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
  }

  public void removeOfficial(Official official, int row) {
    officials.remove(official);
    fireTableRowsDeleted(row, row);
  }
}
