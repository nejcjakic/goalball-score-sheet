package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.Team;

public class TournamentTeamsTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -228900779827062446L;

  // indices of items as located in the table.
  public final static int COLUMN_IDX_TEAM_COUNTRY = 0;
  public final static int COLUMN_IDX_TEAM_NAME = 1;
  public final static int COLUMN_IDX_TEAM_GENDER = 2;

  private final String columnNames[] = { "Country", "Name", "Gender" };

  private List<Team> teams;

  public TournamentTeamsTableModel(List<Team> teams) {
    this.teams = teams;
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
    return teams.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Team team = teams.get(rowIndex);

    switch (columnIndex) {
    case COLUMN_IDX_TEAM_COUNTRY:
      return team.getCountry();
    case COLUMN_IDX_TEAM_NAME:
      return team.getTeamName();
    case COLUMN_IDX_TEAM_GENDER:
      return team.isMale() ? "Male" : "Female";
    default:
      return null;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    Team team = teams.get(row);
    switch (column) {
    case COLUMN_IDX_TEAM_COUNTRY:
      team.setCountry(value.toString());
      break;
    case COLUMN_IDX_TEAM_NAME:
      team.setTeamName(value.toString());
      break;
    case COLUMN_IDX_TEAM_GENDER:
      boolean isMale = "Male".equals(value.toString());
      team.setMale(isMale);
    default:
      break;
    }
    fireTableCellUpdated(row, column);
  }

  public Team getTeam(int index) {
    return teams.get(index);
  }

  public void addTeam(Team team) {
    teams.add(team);
    fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
  }

  public void removeTeam(Team team, int row) {
    teams.remove(team);
    fireTableRowsDeleted(row, row);
  }
}
