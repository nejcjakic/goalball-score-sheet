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

import si.nejcj.goalball.scoresheet.util.Constants.TournamentListeners;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentTableModels;

@SuppressWarnings("serial")
public class TournamentGamesPanel extends JPanel {

  private JTable allGamesTBL;

  public TournamentGamesPanel(
      Map<TournamentListeners, EventListener> componenentListeners,
      Map<TournamentTableModels, TableModel> tableModels) {

    JLabel tournamentGamesLBL = new JLabel("Tournament games");

    JScrollPane allGamesSP = new JScrollPane();

    JButton gameAddBTN = new JButton(
        (AbstractAction) componenentListeners.get(TournamentListeners.GAME_ADD));
    JButton gameEditBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.GAME_EDIT));
    JButton gameRemoveBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.GAME_REMOVE));

    JButton gameResultsBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.GAME_RESULTS));
    
    JButton gameImportBTN = new JButton(
        (AbstractAction) componenentListeners
            .get(TournamentListeners.GAME_IMPORT));

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addComponent(allGamesSP, GroupLayout.DEFAULT_SIZE, 391,
                        Short.MAX_VALUE).addComponent(tournamentGamesLBL))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(gameAddBTN).addComponent(gameEditBTN)
                    .addComponent(gameRemoveBTN).addComponent(gameResultsBTN)
                    .addComponent(gameImportBTN))
            .addContainerGap()));
    groupLayout
        .setVerticalGroup(groupLayout
            .createParallelGroup(Alignment.LEADING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tournamentGamesLBL)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(allGamesSP, GroupLayout.DEFAULT_SIZE, 250,
                        Short.MAX_VALUE).addGap(19))
            .addGroup(
                groupLayout.createSequentialGroup().addGap(73)
                    .addComponent(gameAddBTN)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(gameEditBTN)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(gameRemoveBTN).addGap(31)
                    .addComponent(gameResultsBTN).addGap(45)
                    .addComponent(gameImportBTN)
                    .addContainerGap(53, Short.MAX_VALUE)));
    setLayout(groupLayout);
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    allGamesTBL = new JTable(tableModels.get(TournamentTableModels.GAMES_ALL));
    allGamesTBL.getTableHeader().setReorderingAllowed(false);
    allGamesTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
        tableModels.get(TournamentTableModels.GAMES_ALL));
    allGamesTBL.setRowSorter(sorter);
    allGamesSP.setViewportView(allGamesTBL);
    allGamesTBL.doLayout();
  }

  public int getSelectedGamesRow() {
    if (allGamesTBL.getSelectedRow() == -1) {
      return -1;
    }
    return allGamesTBL.convertRowIndexToModel(allGamesTBL.getSelectedRow());
  }

  public int getRowCount() {
    return allGamesTBL.getRowCount();
  }
}
