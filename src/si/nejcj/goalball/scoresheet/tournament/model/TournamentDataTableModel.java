package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.Tournament;
import si.nejcj.goalball.scoresheet.util.Constants;

public class TournamentDataTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -228900779827062446L;

  // indices of items as located in the table.
  public final static int COLUMN_IDX_TOURNAMENT_NAME = 0;
  public final static int COLUMN_IDX_TOURNAMENT_LOCATION = 1;
  public final static int COLUMN_IDX_START_DATE = 2;
  public final static int COLUMN_IDX_END_DATE = 3;

  private final String columnNames[] = { "Tournament name", "Location",
      "Start date", "End date" };

  private List<Tournament> tournaments;

  public TournamentDataTableModel(List<Tournament> tournaments) {
    this.tournaments = tournaments;
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
  public Class<?> getColumnClass(int columnIdx) {
    return String.class;
  }

  @Override
  public int getRowCount() {
    return tournaments.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Tournament tournament = tournaments.get(rowIndex);

    switch (columnIndex) {
    case COLUMN_IDX_TOURNAMENT_NAME:
      return tournament.getTournamentName();
    case COLUMN_IDX_TOURNAMENT_LOCATION:
      return tournament.getLocation();
    case COLUMN_IDX_START_DATE:
      return Constants.DEFAULT_DATE_FORMAT.format(tournament.getStartDate());
    case COLUMN_IDX_END_DATE:
      return Constants.DEFAULT_DATE_FORMAT.format(tournament.getEndDate());
    default:
      return null;
    }
  }

  public Tournament getTournament(int index) {
    return tournaments.get(index);
  }
}