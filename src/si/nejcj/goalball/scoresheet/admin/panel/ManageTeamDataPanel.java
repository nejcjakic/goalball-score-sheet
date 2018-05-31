package si.nejcj.goalball.scoresheet.admin.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import si.nejcj.goalball.scoresheet.admin.model.TeamNamesComboBoxModel;
import si.nejcj.goalball.scoresheet.db.entity.Team;
import si.nejcj.goalball.scoresheet.util.Constants.ManageTeamDataActions;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ManageTeamDataPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private JTable staffTBL;
  private JTable playersTBL;
  private JComboBox selectionCB;

  public void init(TeamNamesComboBoxModel selectionModel,
      ActionListener teamSelectionListener) {
    selectionCB.setModel(selectionModel);
    selectionCB.addActionListener(teamSelectionListener);
    selectionCB.setSelectedIndex(0);
  }

  public ManageTeamDataPanel(
      Map<ManageTeamDataActions, AbstractAction> teamDataPanelActions) {

    JLabel teamNameLBL = new JLabel("Team name");

    selectionCB = new JComboBox();
    selectionCB.setPreferredSize(new Dimension(30, 20));
    selectionCB.setMinimumSize(new Dimension(30, 20));
    selectionCB.setRenderer(new CountryCBRenderer());
    selectionCB.setMaximumRowCount(20);
    teamNameLBL.setLabelFor(selectionCB);
    JButton addTeamBtn = new JButton(
        teamDataPanelActions.get(ManageTeamDataActions.ADD_TEAM_ACTION));
    addTeamBtn.setText("Add team");

    JScrollPane staffSP = new JScrollPane();
    JScrollPane playersSP = new JScrollPane();

    JLabel staffLBL = new JLabel("Staff members");
    JLabel playersLBL = new JLabel("Players");
    JButton addStaffBtn = new JButton(
        teamDataPanelActions.get(ManageTeamDataActions.ADD_STAFF_ACTION));
    JButton deleteStaffBtn = new JButton(
        teamDataPanelActions.get(ManageTeamDataActions.REMOVE_STAFF_ACTION));
    JButton addPlayerBtn = new JButton(
        teamDataPanelActions.get(ManageTeamDataActions.ADD_PLAYER_ACTION));
    JButton deletePlayerBtn = new JButton(
        teamDataPanelActions.get(ManageTeamDataActions.REMOVE_PLAYER_ACTION));

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout
        .setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(teamNameLBL).addGap(18)
                        .addComponent(selectionCB, GroupLayout.PREFERRED_SIZE,
                            150, GroupLayout.PREFERRED_SIZE)
                        .addGap(18).addComponent(addTeamBtn,
                            GroupLayout.PREFERRED_SIZE, 93,
                            GroupLayout.PREFERRED_SIZE))
                    .addComponent(staffLBL).addComponent(playersLBL)
                    .addComponent(playersSP, GroupLayout.DEFAULT_SIZE, 380,
                        Short.MAX_VALUE)
                    .addComponent(staffSP, 0, 0, Short.MAX_VALUE))
                .addGap(18)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(addStaffBtn).addComponent(deleteStaffBtn)
                    .addComponent(addPlayerBtn).addComponent(deletePlayerBtn))
                .addContainerGap(109, Short.MAX_VALUE)));
    groupLayout
        .setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(teamNameLBL)
                    .addComponent(selectionCB, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTeamBtn))
                .addGap(13).addComponent(staffLBL)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(staffSP, GroupLayout.PREFERRED_SIZE, 198,
                    GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED, 31,
                    Short.MAX_VALUE)
                .addComponent(playersLBL)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(playersSP, GroupLayout.PREFERRED_SIZE, 198,
                    GroupLayout.PREFERRED_SIZE)
                .addGap(76))
            .addGroup(groupLayout.createSequentialGroup().addGap(82)
                .addComponent(addStaffBtn)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(deleteStaffBtn).addGap(256)
                .addComponent(addPlayerBtn)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(deletePlayerBtn)
                .addContainerGap(209, Short.MAX_VALUE)));

    staffTBL = new JTable();
    staffTBL.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    staffTBL.getTableHeader().setReorderingAllowed(false);
    staffTBL.setSurrendersFocusOnKeystroke(true);
    staffTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    staffSP.setViewportView(staffTBL);
    staffTBL.doLayout();

    playersTBL = new JTable();
    playersTBL.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    playersTBL.getTableHeader().setReorderingAllowed(false);
    playersTBL.setSurrendersFocusOnKeystroke(true);
    playersTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playersSP.setViewportView(playersTBL);
    playersTBL.doLayout();

    setLayout(groupLayout);
  }

  public int getSelectedStaffRow() {
    if (staffTBL.getSelectedRow() == -1) {
      return -1;
    }
    return staffTBL.convertRowIndexToModel(staffTBL.getSelectedRow());
  }

  public int getSelectedPlayerRow() {
    if (playersTBL.getSelectedRow() == -1) {
      return -1;
    }
    return playersTBL.convertRowIndexToModel(playersTBL.getSelectedRow());
  }

  class CountryCBRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = -2653934006307979304L;

    public CountryCBRenderer() {
      setOpaque(true);
      setHorizontalAlignment(LEFT);
      setVerticalAlignment(CENTER);
    }

    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      if (isSelected) {
        setBackground(list.getSelectionBackground());
        setForeground(list.getSelectionForeground());
      } else {
        setBackground(list.getBackground());
        setForeground(list.getForeground());
      }

      if (value instanceof Team) {
        Team team = (Team) value;
        if (team.isNationalTeam()) {
          setText(
              team.getCountry() + " / " + (team.isMale() ? "male" : "female"));
        } else {
          setText(team.getTeamName() + " / " + team.getCountry()+ (team.isMale() ? "male" : "female"));
        }
        return this;
      }

      return null;
    }
  }

  public void setStaffTableModel(TableModel model,
      TableModelListener listener) {
    staffTBL.setModel(model);
    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
    staffTBL.setRowSorter(sorter);
    staffTBL.getModel().addTableModelListener(listener);
  }

  public void setPlayersTableModel(TableModel model,
      TableModelListener listener) {
    playersTBL.setModel(model);
    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
    playersTBL.setRowSorter(sorter);
    playersTBL.getModel().addTableModelListener(listener);
  }
}
