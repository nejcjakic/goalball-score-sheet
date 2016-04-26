package si.nejcj.goalball.scoresheet.tournament.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeListener;

import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.db.entity.TournamentOfficial;
import si.nejcj.goalball.scoresheet.db.entity.TournamentTeam;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentOfficialsComboBoxModel;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentTeamsComboBoxModel;
import si.nejcj.goalball.scoresheet.util.Constants;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentGameListeners;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TournamentGameDataPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final ButtonGroup genderButtonGroup = new ButtonGroup();
  private static final Border REQUIRED_COMPONENT_BORDER = BorderFactory
      .createLineBorder(Color.RED);
  private static final String MALE = "Male";
  private static final String FEMALE = "Female";

  private JSpinner gameNumberSP;
  private JComboBox teamACB;
  private JComboBox teamBCB;
  private JComboBox gameDateCB;
  private JTextField timeTF;
  private JTextField poolTF;
  private JTextField venueTF;
  private JRadioButton genderMaleRB;
  private JRadioButton genderFemaleRB;
  private JCheckBox needsWinnerCHB;
  private JComboBox referee1cb;
  private JComboBox referee2cb;
  private JComboBox tenSeconds1CB;
  private JComboBox tenSeconds2CB;
  private JComboBox scorerCB;
  private JComboBox timerCB;
  private JComboBox backupTimerCB;
  private JComboBox goalJudge1CB;
  private JComboBox goalJudge2CB;
  private JComboBox goalJudge3CB;
  private JComboBox goalJudge4CB;

  public TournamentGameDataPanel(
      Map<TournamentGameListeners, EventListener> componentListeners,
      List<TournamentTeam> tournamentTeams,
      List<TournamentOfficial> tournamentOfficials, List<Date> tournamentDates,
      String defaultVenue) {
    setSize(700, 430);
    setPreferredSize(new Dimension(700, 430));

    List<TournamentOfficial> minorOfficials = new ArrayList<TournamentOfficial>(
        tournamentOfficials);
    minorOfficials.add(new TournamentOfficial(null, "", "", "", "", null));
    Collections.sort(tournamentTeams);
    Collections.sort(tournamentOfficials);
    Collections.sort(minorOfficials);

    JLabel gameInformationLBL = new JLabel("Game information");
    gameInformationLBL.setFont(new Font("Tahoma", Font.BOLD, 11));

    JLabel teamsLBL = new JLabel("TEAMS");
    JLabel gameNumberLBL = new JLabel("Game number");
    gameNumberSP = new JSpinner();
    gameNumberSP.setModel(new SpinnerNumberModel(new Integer(0),
        new Integer(0), null, new Integer(1)));
    gameNumberSP.addChangeListener((ChangeListener) componentListeners
        .get(TournamentGameListeners.GAME_NUMBER));
    gameNumberSP.setBorder(REQUIRED_COMPONENT_BORDER);
    JLabel teamALBL = new JLabel("Team A");

    teamACB = new JComboBox();
    teamACB.setModel(new TournamentTeamsComboBoxModel(tournamentTeams));
    teamACB.setRenderer(new TournamentTeamCBRenderer());
    teamACB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.TEAM_A));
    teamACB.setBorder(REQUIRED_COMPONENT_BORDER);
    JLabel teamBLBL = new JLabel("Team B");
    teamBCB = new JComboBox();
    teamBCB.setModel(new TournamentTeamsComboBoxModel(tournamentTeams));
    teamBCB.setRenderer(new TournamentTeamCBRenderer());
    teamBCB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.TEAM_B));
    teamBCB.setBorder(REQUIRED_COMPONENT_BORDER);

    JSeparator separator = new JSeparator();

    JLabel gameInfoLBL = new JLabel("GAME INFORMATION");
    JLabel dateLBL = new JLabel("Date");
    gameDateCB = new JComboBox();
    gameDateCB.setModel(new DefaultComboBoxModel(tournamentDates.toArray()));
    gameDateCB.setRenderer(new TournamentDateCBRenderer());
    gameDateCB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.DATE));
    gameDateCB.setBorder(REQUIRED_COMPONENT_BORDER);
    JLabel timeLBL = new JLabel("Time");
    timeTF = new JTextField();
    timeTF.setColumns(10);
    timeTF.addCaretListener((CaretListener) componentListeners
        .get(TournamentGameListeners.TIME));
    timeTF.setBorder(REQUIRED_COMPONENT_BORDER);
    JLabel lblDivisionpool = new JLabel("Division/Pool");
    poolTF = new JTextField();
    poolTF.setColumns(10);
    poolTF.addCaretListener((CaretListener) componentListeners
        .get(TournamentGameListeners.POOL));
    JLabel venueLBL = new JLabel("Venue");
    venueTF = new JTextField(defaultVenue);
    venueTF.setColumns(10);
    venueTF.addCaretListener((CaretListener) componentListeners
        .get(TournamentGameListeners.VENUE));
    JLabel genderLBL = new JLabel("Gender");
    genderMaleRB = new JRadioButton(MALE);
    genderMaleRB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.GENDER));
    genderFemaleRB = new JRadioButton(FEMALE);
    genderFemaleRB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.GENDER));
    genderButtonGroup.add(genderMaleRB);
    genderButtonGroup.add(genderFemaleRB);

    needsWinnerCHB = new JCheckBox("Needs winner");
    needsWinnerCHB.addItemListener((ItemListener) componentListeners
        .get(TournamentGameListeners.NEEDS_WINNER));

    JSeparator separator_1 = new JSeparator();

    JLabel officialsLBL = new JLabel("OFFICIALS");
    JLabel referee1LBL = new JLabel("Referee");
    referee1cb = new JComboBox();
    referee1cb.setModel(new TournamentOfficialsComboBoxModel(
        tournamentOfficials));
    referee1cb.setRenderer(new TournamentOfficialCBRenderer());
    referee1cb.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.REFEREE_1));
    referee1cb.setBorder(REQUIRED_COMPONENT_BORDER);
    JLabel referee2LBL = new JLabel("Referee");
    referee2cb = new JComboBox();
    referee2cb.setModel(new TournamentOfficialsComboBoxModel(
        tournamentOfficials));
    referee2cb.setRenderer(new TournamentOfficialCBRenderer());
    referee2cb.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.REFEREE_2));
    referee2cb.setBorder(REQUIRED_COMPONENT_BORDER);
    JLabel tenSeconds1LBL = new JLabel("10 seconds");
    tenSeconds1CB = new JComboBox();
    tenSeconds1CB.setModel(new TournamentOfficialsComboBoxModel(
        tournamentOfficials));
    tenSeconds1CB.setRenderer(new TournamentOfficialCBRenderer());
    tenSeconds1CB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.TEN_SEC_1));
    JLabel tenSeconds2LBL = new JLabel("10 seconds");
    tenSeconds2CB = new JComboBox();
    tenSeconds2CB.setModel(new TournamentOfficialsComboBoxModel(
        tournamentOfficials));
    tenSeconds2CB.setRenderer(new TournamentOfficialCBRenderer());
    tenSeconds2CB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.TEN_SEC_2));
    JLabel scorerLBL = new JLabel("Scorer");
    scorerCB = new JComboBox();
    scorerCB
        .setModel(new TournamentOfficialsComboBoxModel(tournamentOfficials));
    scorerCB.setRenderer(new TournamentOfficialCBRenderer());
    scorerCB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.SCORER));
    JLabel timerLBL = new JLabel("Timer");
    timerCB = new JComboBox();
    timerCB.setModel(new TournamentOfficialsComboBoxModel(tournamentOfficials));
    timerCB.setRenderer(new TournamentOfficialCBRenderer());
    timerCB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.TIMER));
    JLabel backupTimerLBL = new JLabel("Backup timer");
    backupTimerCB = new JComboBox();
    backupTimerCB
        .setModel(new TournamentOfficialsComboBoxModel(minorOfficials));
    backupTimerCB.setRenderer(new TournamentOfficialCBRenderer());
    backupTimerCB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.BACKUP_TIMER));
    JLabel goalJudge1LBL = new JLabel("Goal judge");
    goalJudge1CB = new JComboBox();
    goalJudge1CB.setModel(new TournamentOfficialsComboBoxModel(minorOfficials));
    goalJudge1CB.setRenderer(new TournamentOfficialCBRenderer());
    goalJudge1CB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.GOAL_JUDGE_1));
    JLabel goalJudge2LBL = new JLabel("Goal judge");
    goalJudge2CB = new JComboBox();
    goalJudge2CB.setModel(new TournamentOfficialsComboBoxModel(minorOfficials));
    goalJudge2CB.setRenderer(new TournamentOfficialCBRenderer());
    goalJudge2CB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.GOAL_JUDGE_2));
    JLabel goalJudge3LBL = new JLabel("Goal judge");
    goalJudge3CB = new JComboBox();
    goalJudge3CB.setModel(new TournamentOfficialsComboBoxModel(minorOfficials));
    goalJudge3CB.setRenderer(new TournamentOfficialCBRenderer());
    goalJudge3CB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.GOAL_JUDGE_3));
    JLabel goalJudge4LBL = new JLabel("Goal judge");
    goalJudge4CB = new JComboBox();
    goalJudge4CB.setModel(new TournamentOfficialsComboBoxModel(minorOfficials));
    goalJudge4CB.setRenderer(new TournamentOfficialCBRenderer());
    goalJudge4CB.addActionListener((ActionListener) componentListeners
        .get(TournamentGameListeners.GOAL_JUDGE_4));

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.TRAILING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(separator, GroupLayout.PREFERRED_SIZE, 680, Short.MAX_VALUE)
            .addComponent(gameInformationLBL)
            .addGroup(groupLayout.createSequentialGroup()
              .addComponent(gameNumberLBL)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(gameNumberSP, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
              .addGap(18)
              .addComponent(teamALBL)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(teamACB, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
              .addGap(18)
              .addComponent(teamBLBL)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(teamBCB, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
            .addComponent(teamsLBL)
            .addComponent(gameInfoLBL)
            .addGroup(groupLayout.createSequentialGroup()
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(dateLBL)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(gameDateCB, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                  .addGap(18)
                  .addComponent(timeLBL)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(timeTF, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(venueLBL)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(venueTF)))
              .addGap(18)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(genderLBL)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(genderMaleRB)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(genderFemaleRB)
                  .addGap(18)
                  .addComponent(needsWinnerCHB))
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(lblDivisionpool)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(poolTF, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))))
            .addComponent(officialsLBL)
            .addGroup(groupLayout.createSequentialGroup()
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(goalJudge1LBL)
                .addComponent(scorerLBL)
                .addComponent(referee1LBL))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(scorerCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addComponent(timerLBL))
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(referee1cb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addComponent(referee2LBL))
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(goalJudge1CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addComponent(goalJudge2LBL)))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                  .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(referee2cb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(timerCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(backupTimerLBL)
                    .addComponent(tenSeconds1LBL)))
                .addGroup(groupLayout.createSequentialGroup()
                  .addComponent(goalJudge2CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addComponent(goalJudge3LBL)))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                .addComponent(goalJudge3CB, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backupTimerCB, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tenSeconds1CB, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
              .addPreferredGap(ComponentPlacement.UNRELATED)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(tenSeconds2LBL)
                .addComponent(goalJudge4LBL))
              .addGap(18)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                .addComponent(goalJudge4CB, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tenSeconds2CB, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
              .addPreferredGap(ComponentPlacement.RELATED, 43, Short.MAX_VALUE))
            .addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 680, Short.MAX_VALUE))
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(gameInformationLBL)
          .addGap(40)
          .addComponent(teamsLBL)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(gameNumberLBL)
            .addComponent(gameNumberSP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(teamALBL)
            .addComponent(teamACB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(teamBLBL)
            .addComponent(teamBCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(separator, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
          .addGap(18)
          .addComponent(gameInfoLBL)
          .addGap(18)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(dateLBL)
            .addComponent(gameDateCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(timeLBL)
            .addComponent(timeTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(lblDivisionpool)
            .addComponent(poolTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addPreferredGap(ComponentPlacement.RELATED)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(venueLBL)
            .addComponent(venueTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(genderLBL)
            .addComponent(genderMaleRB)
            .addComponent(genderFemaleRB)
            .addComponent(needsWinnerCHB))
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addGap(20)
          .addComponent(officialsLBL)
          .addGap(18)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(referee1LBL)
            .addComponent(referee1cb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(referee2LBL)
            .addComponent(referee2cb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(tenSeconds1LBL)
            .addComponent(tenSeconds1CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(tenSeconds2LBL)
            .addComponent(tenSeconds2CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(scorerLBL)
            .addComponent(scorerCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(timerLBL)
            .addComponent(timerCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(backupTimerLBL)
            .addComponent(backupTimerCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(goalJudge1LBL)
            .addComponent(goalJudge1CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(goalJudge2LBL)
            .addComponent(goalJudge2CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(goalJudge3LBL)
            .addComponent(goalJudge3CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(goalJudge4LBL)
            .addComponent(goalJudge4CB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addGap(73))
    );
    setLayout(groupLayout);
  }

  public void selectDefaultData() {
    gameDateCB.setSelectedIndex(0);
    genderMaleRB.doClick();
  }

  public void setGameData(TournamentGame game) {
    gameNumberSP.setValue(game.getGameNumber());
    teamACB.setSelectedItem(game.getTeamA());
    teamBCB.setSelectedItem(game.getTeamB());
    gameDateCB.setSelectedItem(game.getGameDate());
    timeTF.setText(game.getGameTime());
    poolTF.setText(game.getPool());
    venueTF.setText(game.getVenue());
    referee1cb.setSelectedItem(game.getReferee1());
    referee2cb.setSelectedItem(game.getReferee2());
    tenSeconds1CB.setSelectedItem(game.getTenSeconds1());
    tenSeconds2CB.setSelectedItem(game.getTenSeconds2());
    scorerCB.setSelectedItem(game.getScorer());
    timerCB.setSelectedItem(game.getTimer());
    backupTimerCB.setSelectedItem(game.getBackupTimer());
    goalJudge1CB.setSelectedItem(game.getGoalJudge1());
    goalJudge2CB.setSelectedItem(game.getGoalJudge2());
    goalJudge3CB.setSelectedItem(game.getGoalJudge3());
    goalJudge4CB.setSelectedItem(game.getGoalJudge4());

    String gender = game.getGender();
    if (MALE.equals(gender)) {
      genderMaleRB.doClick();
    } else {
      genderFemaleRB.doClick();
    }
    needsWinnerCHB.setSelected(game.getNeedsWinner());
  }

  class TournamentOfficialCBRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = 1L;

    public TournamentOfficialCBRenderer() {
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

      if (value != null && value instanceof TournamentOfficial) {
        TournamentOfficial official = (TournamentOfficial) value;
        setText(official.getFullName());
        return this;
      }

      setText("");
      return this;
    }
  }

  class TournamentTeamCBRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = 1L;

    public TournamentTeamCBRenderer() {
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

      if (value != null && value instanceof TournamentTeam) {
        TournamentTeam team = (TournamentTeam) value;
        setText(team.getDisplayName());
        return this;
      }

      setText("");
      return this;
    }
  }

  class TournamentDateCBRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = 1L;

    public TournamentDateCBRenderer() {
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

      if (value != null && value instanceof Date) {
        Date date = (Date) value;
        setText(Constants.DEFAULT_DATE_FORMAT.format(date));
        return this;
      }

      setText("");
      return this;
    }
  }
}
