package si.nejcj.goalball.scoresheet.tournament.panel;

import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.EventListener;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.CaretListener;

import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

import si.nejcj.goalball.scoresheet.db.entity.Tournament;
import si.nejcj.goalball.scoresheet.util.Constants;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentListeners;

public class TournamentPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private static final int IDX_TOURNAMENT_DATA = 0;
  private static final int IDX_TOURNAMENT_GAMES = 1;

  private TournamentDataPanel tournamentDataPanel;
  private TournamentGamesPanel tournamentGamesPanel;

  private JTextField tournamentNameTF;
  private JTextField locationTF;
  private JTextField startDateTF;
  private JCalendarButton startDateCB;
  private JTextField endDateTF;
  private JCalendarButton endDateCB;
  private JTabbedPane tournamentTP;

  public TournamentPanel(
      Map<TournamentListeners, EventListener> componenentListeners) {
    JLabel tournamentInformationLBL = new JLabel("TOURNAMENT INFORMATION");
    tournamentInformationLBL.setFont(new Font("Tahoma", Font.BOLD, 14));
    JLabel tournamentNameLBL = new JLabel("Tournament name");
    tournamentNameTF = new JTextField();
    tournamentNameTF.setEditable(false);
    tournamentNameTF.setColumns(10);
    tournamentNameTF.addCaretListener((CaretListener) componenentListeners
        .get(TournamentListeners.TOURNAMENT_NAME));
    JLabel locationLBL = new JLabel("Location");
    locationTF = new JTextField();
    locationTF.setEditable(false);
    locationTF.setColumns(10);
    locationTF.addCaretListener((CaretListener) componenentListeners
        .get(TournamentListeners.TOURNAMENT_LOCATION));
    JLabel startDateLBL = new JLabel("Start date");
    startDateTF = new JTextField();
    startDateTF.setEditable(false);
    startDateTF.setColumns(10);
    startDateCB = new JCalendarButton();
    startDateCB.setEnabled(false);
    startDateCB
        .addPropertyChangeListener((PropertyChangeListener) componenentListeners
            .get(TournamentListeners.TOURNAMENT_START_DATE));
    JLabel endDateLBL = new JLabel("End date");
    endDateTF = new JTextField();
    endDateTF.setEditable(false);
    endDateTF.setColumns(10);
    endDateCB = new JCalendarButton();
    endDateCB.setEnabled(false);
    endDateCB
        .addPropertyChangeListener((PropertyChangeListener) componenentListeners
            .get(TournamentListeners.TOURNAMENT_END_DATE));

    tournamentTP = new JTabbedPane(JTabbedPane.TOP);
    tournamentTP.addTab("Tournament data", null);
    tournamentTP.addTab("Tournament games", null);

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
        Alignment.TRAILING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addComponent(tournamentTP, GroupLayout.DEFAULT_SIZE, 330,
                        Short.MAX_VALUE)
                    .addComponent(tournamentInformationLBL)
                    .addGroup(
                        groupLayout.createSequentialGroup()
                            .addComponent(tournamentNameLBL)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(tournamentNameTF, 239, 239, 239))
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addComponent(locationLBL)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(locationTF,
                                GroupLayout.PREFERRED_SIZE, 286,
                                GroupLayout.PREFERRED_SIZE))
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addComponent(startDateLBL)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(startDateTF,
                                GroupLayout.PREFERRED_SIZE, 86,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(startDateCB,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(endDateLBL)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(endDateTF,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(endDateCB,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))).addGap(110)));
    groupLayout
        .setVerticalGroup(groupLayout
            .createParallelGroup(Alignment.LEADING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        groupLayout
                            .createParallelGroup(Alignment.TRAILING)
                            .addComponent(endDateCB,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                groupLayout
                                    .createSequentialGroup()
                                    .addComponent(tournamentInformationLBL)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(
                                        groupLayout
                                            .createParallelGroup(
                                                Alignment.BASELINE)
                                            .addComponent(tournamentNameLBL)
                                            .addComponent(tournamentNameTF,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(
                                        groupLayout
                                            .createParallelGroup(
                                                Alignment.TRAILING)
                                            .addGroup(
                                                groupLayout
                                                    .createSequentialGroup()
                                                    .addGroup(
                                                        groupLayout
                                                            .createParallelGroup(
                                                                Alignment.BASELINE)
                                                            .addComponent(
                                                                locationLBL)
                                                            .addComponent(
                                                                locationTF,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(
                                                        ComponentPlacement.RELATED)
                                                    .addGroup(
                                                        groupLayout
                                                            .createParallelGroup(
                                                                Alignment.BASELINE)
                                                            .addComponent(
                                                                startDateTF,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(
                                                                startDateLBL)
                                                            .addComponent(
                                                                endDateLBL)
                                                            .addComponent(
                                                                endDateTF,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)))
                                            .addComponent(startDateCB,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(tournamentTP, GroupLayout.DEFAULT_SIZE, 168,
                        Short.MAX_VALUE).addGap(20)));
    setLayout(groupLayout);
  }

  public void enableAllFields() {
    tournamentNameTF.setEditable(true);
    locationTF.setEditable(true);
    startDateCB.setEnabled(true);
    endDateCB.setEnabled(true);
  }

  public void setTournamentFields(final Tournament tournament) {
    setTournamentName(tournament.getTournamentName());
    setTournamentLocation(tournament.getLocation());
    setStartDate(tournament.getStartDate());
    setEndDate(tournament.getEndDate());
  }

  public void setTournamentName(final String name) {
    tournamentNameTF.setText(name);
  }

  public void setTournamentLocation(final String location) {
    locationTF.setText(location);
  }

  public void setStartDate(Date date) {
    startDateTF.setText(Constants.DEFAULT_DATE_FORMAT.format(date));
    startDateCB.setTargetDate(date);
  }

  public void setEndDate(Date date) {
    endDateTF.setText(Constants.DEFAULT_DATE_FORMAT.format(date));
    endDateCB.setTargetDate(date);
  }

  public void setTournamentDataPanel(TournamentDataPanel dataPanel) {
    tournamentDataPanel = dataPanel;
    tournamentTP.setComponentAt(IDX_TOURNAMENT_DATA, dataPanel);
  }

  public void setTournamentGamesPanel(TournamentGamesPanel gamesPanel) {
    tournamentGamesPanel = gamesPanel;
    tournamentTP.setComponentAt(IDX_TOURNAMENT_GAMES, gamesPanel);
  }

  public int getSelectedGamesRow() {
    return tournamentGamesPanel.getSelectedGamesRow();
  }
  
  public int getNumberOfGames() {
    return tournamentGamesPanel.getRowCount();
  }

  public int getSelectedAllTeamsRow() {
    return tournamentDataPanel.getSelectedAllTeamsRow();
  }

  public int getSelectedParticipatingTeamsRow() {
    return tournamentDataPanel.getSelectedParticipatingTeamsRow();
  }

  public int getSelectedAllOfficialsRow() {
    return tournamentDataPanel.getSelectedAllOfficialsRow();
  }

  public int getSelectedTournamentOfficialsRow() {
    return tournamentDataPanel.getSelectedTournamentOfficialsRow();
  }
}
