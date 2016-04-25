package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.TournamentStaff;

public class TournamentStaffMembersTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;

  // indices of items as located in the table.
  // indices of items as located in the table.
  public final static int COLUMN_IDX_PARTICIPATING = 0;
  public final static int COLUMN_IDX_LAST_NAME = 1;
  public final static int COLUMN_IDX_FIRST_NAME = 2;
  public final static int COLUMN_IDX_POSITION = 3;

  private final String columnNames[] = { "Participating", "Last name",
      "First name", "Position" };

  private List<TournamentStaff> allStaffMembers;
  private List<TournamentStaff> selectedStaffMembers;

  public TournamentStaffMembersTableModel(List<TournamentStaff> staffMembers) {
    this.allStaffMembers = staffMembers;
    this.selectedStaffMembers = new ArrayList<TournamentStaff>();
  }

  public TournamentStaffMembersTableModel(List<TournamentStaff> staffMembers,
      List<TournamentStaff> selectedStaffMembers) {
    this.allStaffMembers = staffMembers;
    this.selectedStaffMembers = selectedStaffMembers;
    for (TournamentStaff tournamentStaff : selectedStaffMembers) {
      for (TournamentStaff staff : allStaffMembers) {
        if (tournamentStaff.isEqualPerson(staff)) {
          tournamentStaff.setId(staff.getId());
          allStaffMembers.remove(staff);
          allStaffMembers.add(tournamentStaff);
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
    if (columnIndex == COLUMN_IDX_PARTICIPATING) {
      return Boolean.class;
    }
    return String.class;
  }

  @Override
  public int getRowCount() {
    return allStaffMembers.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if (col == COLUMN_IDX_PARTICIPATING) {
      return true;
    }
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    TournamentStaff staffMember = allStaffMembers.get(rowIndex);
    switch (columnIndex) {
    case COLUMN_IDX_LAST_NAME:
      return staffMember.getLastName();
    case COLUMN_IDX_FIRST_NAME:
      return staffMember.getFirstName();
    case COLUMN_IDX_POSITION:
      return staffMember.getPersonPosition();
    case COLUMN_IDX_PARTICIPATING:
      return selectedStaffMembers.contains(staffMember);
    default:
      return null;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    TournamentStaff staffMember = allStaffMembers.get(row);
    if (column == COLUMN_IDX_PARTICIPATING) {
      if (value instanceof Boolean) {
        boolean selected = (Boolean) value;
        if (selected) {
          selectedStaffMembers.add(staffMember);
        } else {
          selectedStaffMembers.remove(staffMember);
        }
      }
    }
    fireTableCellUpdated(row, column);
  }

  public List<TournamentStaff> getSelectedStaffMembers() {
    return selectedStaffMembers;
  }
}
