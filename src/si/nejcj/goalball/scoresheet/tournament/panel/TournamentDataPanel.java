package si.nejcj.goalball.scoresheet.tournament.panel;

import java.util.EventListener;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentListeners;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentTableModels;
import si.nejcj.goalball.scoresheet.util.gui.OfficialsLevelTableRenderer;

@SuppressWarnings("serial")
public class TournamentDataPanel extends JPanel {

  private JTable participatingTeamsTBL;
  private JTable allTeamsTBL;

  private JTable tournamentOfficialsTBL;
  private JTable allOfficialsTBL;

  public TournamentDataPanel(
      Map<TournamentListeners, EventListener> componenentListeners,
      Map<TournamentTableModels, TableModel> tableModels) {

    JLabel participatingTeamsLBL = new JLabel("Participating teams");
    JLabel allTeamsLBL = new JLabel("All teams");
    JButton teamAddBTN = new JButton(
        (AbstractAction) componenentListeners.get(TournamentListeners.TEAM_ADD));
    JButton teamRemoveBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.TEAM_REMOVE));
    JButton teamEditBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.TEAM_EDIT));
    JScrollPane participatingTeamsSP = new JScrollPane();
    JScrollPane allTeamsSP = new JScrollPane();

    JLabel tournamentOfficialsLBL = new JLabel("Tournament officials");
    JLabel allOfficialsLBL = new JLabel("All officials");
    JButton officialAddBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.OFFICIAL_ADD));
    JButton officialRemoveBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.OFFICIAL_REMOVE));
    JScrollPane tournamentOfficialsSP = new JScrollPane();
    JScrollPane allOfficialsSP = new JScrollPane();

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addComponent(participatingTeamsSP,
                                GroupLayout.PREFERRED_SIZE, 250,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(
                                groupLayout
                                    .createParallelGroup(Alignment.LEADING)
                                    .addComponent(teamEditBTN)
                                    .addComponent(teamRemoveBTN)
                                    .addComponent(teamAddBTN)))
                    .addComponent(participatingTeamsLBL)
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addComponent(tournamentOfficialsSP,
                                GroupLayout.PREFERRED_SIZE, 250,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(
                                groupLayout
                                    .createParallelGroup(Alignment.LEADING)
                                    .addComponent(officialRemoveBTN)
                                    .addComponent(officialAddBTN)))
                    .addComponent(tournamentOfficialsLBL))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addComponent(allOfficialsLBL)
                    .addComponent(allTeamsSP, GroupLayout.PREFERRED_SIZE, 250,
                        GroupLayout.PREFERRED_SIZE)
                    .addComponent(allTeamsLBL)
                    .addComponent(allOfficialsSP, GroupLayout.PREFERRED_SIZE,
                        250, GroupLayout.PREFERRED_SIZE))
            .addContainerGap(45, Short.MAX_VALUE)));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                groupLayout
                                    .createParallelGroup(Alignment.BASELINE)
                                    .addComponent(participatingTeamsLBL)
                                    .addComponent(allTeamsLBL))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(
                                groupLayout
                                    .createParallelGroup(Alignment.BASELINE)
                                    .addComponent(participatingTeamsSP,
                                        GroupLayout.PREFERRED_SIZE, 184,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addComponent(allTeamsSP,
                                        GroupLayout.PREFERRED_SIZE, 184,
                                        GroupLayout.PREFERRED_SIZE)))
                    .addGroup(
                        groupLayout.createSequentialGroup().addGap(81)
                            .addComponent(teamAddBTN)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(teamRemoveBTN)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(teamEditBTN)))
            .addPreferredGap(ComponentPlacement.UNRELATED)
            .addGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(tournamentOfficialsLBL)
                    .addComponent(allOfficialsLBL))
            .addGap(6)
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addComponent(tournamentOfficialsSP,
                        GroupLayout.PREFERRED_SIZE, 184,
                        GroupLayout.PREFERRED_SIZE)
                    .addGroup(
                        groupLayout.createSequentialGroup().addGap(42)
                            .addComponent(officialAddBTN)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(officialRemoveBTN))
                    .addComponent(allOfficialsSP, GroupLayout.PREFERRED_SIZE,
                        184, GroupLayout.PREFERRED_SIZE))
            .addContainerGap(34, Short.MAX_VALUE)));
    setLayout(groupLayout);
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    participatingTeamsTBL = new JTable(
        tableModels.get(TournamentTableModels.TEAMS_PARTICIPATING));
    participatingTeamsTBL.getTableHeader().setReorderingAllowed(false);
    participatingTeamsTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RowSorter<TableModel> participatingTeamsSorter = new TableRowSorter<TableModel>(
        tableModels.get(TournamentTableModels.TEAMS_PARTICIPATING));
    participatingTeamsTBL.setRowSorter(participatingTeamsSorter);
    participatingTeamsSP.setViewportView(participatingTeamsTBL);
    participatingTeamsTBL.doLayout();

    allTeamsTBL = new JTable(tableModels.get(TournamentTableModels.TEAMS_ALL));
    allTeamsTBL.getTableHeader().setReorderingAllowed(false);
    allTeamsTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RowSorter<TableModel> allTeamsSorter = new TableRowSorter<TableModel>(
        tableModels.get(TournamentTableModels.TEAMS_ALL));
    allTeamsTBL.setRowSorter(allTeamsSorter);
    allTeamsSP.setViewportView(allTeamsTBL);
    allTeamsTBL.doLayout();

    tournamentOfficialsTBL = new JTable(
        tableModels.get(TournamentTableModels.OFFICIALS_TOURNAMENT));
    tournamentOfficialsTBL.setDefaultRenderer(OfficialLevel.class,
        new OfficialsLevelTableRenderer());
    tournamentOfficialsTBL.getTableHeader().setReorderingAllowed(false);
    tournamentOfficialsTBL
        .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RowSorter<TableModel> tournamentOfficialsSorter = new TableRowSorter<TableModel>(
        tableModels.get(TournamentTableModels.OFFICIALS_TOURNAMENT));
    tournamentOfficialsTBL.setRowSorter(tournamentOfficialsSorter);
    tournamentOfficialsSP.setViewportView(tournamentOfficialsTBL);
    tournamentOfficialsTBL.doLayout();

    allOfficialsTBL = new JTable(
        tableModels.get(TournamentTableModels.OFFICIALS_ALL));
    allOfficialsTBL.setDefaultRenderer(OfficialLevel.class,
        new OfficialsLevelTableRenderer());
    allOfficialsTBL.getTableHeader().setReorderingAllowed(false);
    allOfficialsTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RowSorter<TableModel> allOfficialsSorter = new TableRowSorter<TableModel>(
        tableModels.get(TournamentTableModels.OFFICIALS_ALL));
    allOfficialsTBL.setRowSorter(allOfficialsSorter);
    allOfficialsSP.setViewportView(allOfficialsTBL);
    allOfficialsTBL.doLayout();
  }

  public int getSelectedParticipatingTeamsRow() {
    if (participatingTeamsTBL.getSelectedRow() == -1) {
      return -1;
    }
    return participatingTeamsTBL.convertRowIndexToModel(participatingTeamsTBL
        .getSelectedRow());
  }

  public int getSelectedAllTeamsRow() {
    if (allTeamsTBL.getSelectedRow() == -1) {
      return -1;
    }
    return allTeamsTBL.convertRowIndexToModel(allTeamsTBL.getSelectedRow());
  }

  public int getSelectedTournamentOfficialsRow() {
    if (tournamentOfficialsTBL.getSelectedRow() == -1) {
      return -1;
    }
    return tournamentOfficialsTBL.convertRowIndexToModel(tournamentOfficialsTBL
        .getSelectedRow());
  }

  public int getSelectedAllOfficialsRow() {
    if (allOfficialsTBL.getSelectedRow() == -1) {
      return -1;
    }
    return allOfficialsTBL.convertRowIndexToModel(allOfficialsTBL
        .getSelectedRow());
  }
}
