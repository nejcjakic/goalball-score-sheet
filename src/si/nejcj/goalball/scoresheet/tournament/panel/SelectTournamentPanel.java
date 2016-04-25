package si.nejcj.goalball.scoresheet.tournament.panel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")
public class SelectTournamentPanel extends JPanel {

  private JTable existingTournamentsTBL;

  public SelectTournamentPanel(TableModel tournamentsTableModel) {

    JLabel existingTournamentsLBL = new JLabel("Existing tournaments");

    JScrollPane existingTournamentsSP = new JScrollPane();
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(existingTournamentsSP)
                    .addComponent(existingTournamentsLBL)).addGap(38)));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addComponent(existingTournamentsLBL)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(existingTournamentsSP, GroupLayout.DEFAULT_SIZE, 358,
                Short.MAX_VALUE).addContainerGap()));
    setLayout(groupLayout);

    existingTournamentsTBL = new JTable(tournamentsTableModel);
    existingTournamentsTBL.putClientProperty("terminateEditOnFocusLost",
        Boolean.TRUE);
    existingTournamentsTBL.getTableHeader().setReorderingAllowed(false);
    existingTournamentsTBL.setSurrendersFocusOnKeystroke(true);
    existingTournamentsTBL
        .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
        tournamentsTableModel);
    existingTournamentsTBL.setRowSorter(sorter);
    existingTournamentsSP.setViewportView(existingTournamentsTBL);
  }

  public int getSelectedRow() {
    if (existingTournamentsTBL.getSelectedRow() == -1) {
      return -1;
    }
    return existingTournamentsTBL.convertRowIndexToModel(existingTournamentsTBL
        .getSelectedRow());
  }
}
