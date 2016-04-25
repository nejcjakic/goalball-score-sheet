package si.nejcj.goalball.scoresheet.admin.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import si.nejcj.goalball.scoresheet.db.entity.Staff;

public class StaffMembersTableModel extends AbstractTableModel {

  /**
   * 
   */
  private static final long serialVersionUID = -7931557450273823496L;

  // indices of items as located in the table.
  public final static int COLUMN_IDX_LAST_NAME = 0;
  public final static int COLUMN_IDX_FIRST_NAME = 1;
  public final static int COLUMN_IDX_POSITION = 2;

  protected String columnNames[] = { "Last name", "First name", "Position" };

  protected List<Staff> staffMembers;

  public StaffMembersTableModel(List<Staff> staffMembers) {
    this.staffMembers = staffMembers;
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
    return staffMembers.size();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return true;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Staff staffMember = staffMembers.get(rowIndex);
    switch (columnIndex) {
    case COLUMN_IDX_LAST_NAME:
      return staffMember.getLastName();
    case COLUMN_IDX_FIRST_NAME:
      return staffMember.getFirstName();
    case COLUMN_IDX_POSITION:
      return staffMember.getPersonPosition();
    default:
      return null;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    Staff staffMember = staffMembers.get(row);
    switch (column) {
    case COLUMN_IDX_LAST_NAME:
      staffMember.setLastName(value.toString());
      break;
    case COLUMN_IDX_FIRST_NAME:
      staffMember.setFirstName(value.toString());
      break;
    case COLUMN_IDX_POSITION:
      staffMember.setPersonPosition(value.toString());
      break;
    default:
      break;
    }
    fireTableCellUpdated(row, column);
  }

  public Staff getStaffMember(int index) {
    return staffMembers.get(index);
  }

  public void addStaffMember(Staff staffMember) {
    staffMembers.add(staffMember);
    fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
  }

  public void removeStaffMember(Staff staffMember, int row) {
    staffMembers.remove(staffMember);
    fireTableRowsDeleted(row, row);
  }
}
