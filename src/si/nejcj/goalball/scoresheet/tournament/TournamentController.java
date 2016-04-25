package si.nejcj.goalball.scoresheet.tournament;

import static si.nejcj.goalball.scoresheet.util.PdfFieldConstants.FIELD_TEAM_A_NAME;
import static si.nejcj.goalball.scoresheet.util.PdfFieldConstants.FIELD_TEAM_B_NAME;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import si.nejcj.goalball.scoresheet.db.DatabaseConnection;
import si.nejcj.goalball.scoresheet.db.entity.Official;
import si.nejcj.goalball.scoresheet.db.entity.Team;
import si.nejcj.goalball.scoresheet.db.entity.Tournament;
import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.db.entity.TournamentOfficial;
import si.nejcj.goalball.scoresheet.db.entity.TournamentPlayer;
import si.nejcj.goalball.scoresheet.db.entity.TournamentStaff;
import si.nejcj.goalball.scoresheet.db.entity.TournamentTeam;
import si.nejcj.goalball.scoresheet.db.entity.util.GameResult;
import si.nejcj.goalball.scoresheet.db.entity.util.TournamentStats;
import si.nejcj.goalball.scoresheet.exception.business.InternalIntegrityConstraintException;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentGamesTableModel;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentOfficialsTableModel;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentPlayersTableModel;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentStaffMembersTableModel;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentTeamsTableModel;
import si.nejcj.goalball.scoresheet.tournament.panel.GameResultsPanel;
import si.nejcj.goalball.scoresheet.tournament.panel.ManageTournamentTeamPanel;
import si.nejcj.goalball.scoresheet.tournament.panel.RefereeSchedulePanel;
import si.nejcj.goalball.scoresheet.tournament.panel.TournamentDataPanel;
import si.nejcj.goalball.scoresheet.tournament.panel.TournamentGameDataPanel;
import si.nejcj.goalball.scoresheet.tournament.panel.TournamentGamesPanel;
import si.nejcj.goalball.scoresheet.tournament.panel.TournamentPanel;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentGameComboBoxType;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentGameListeners;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentGameTextType;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentListeners;
import si.nejcj.goalball.scoresheet.util.Constants.TournamentTableModels;
import si.nejcj.goalball.scoresheet.util.ErrorHandler;
import si.nejcj.goalball.scoresheet.util.PdfFieldConstants;
import si.nejcj.goalball.scoresheet.util.PdfFieldConverter;
import si.nejcj.goalball.scoresheet.util.PdfUtil;

public class TournamentController {
  private DatabaseConnection m_dbConnection;
  private ClassLoader m_classLoader;

  private static File m_tournamentResultsSaveDir;
  private static File m_gameSheetsSaveDir;
  private static File m_tournamentStatsSaveDir;

  private TournamentPanel m_tournamentPanel;
  private Tournament m_tournament;
  private TournamentTeamsTableModel m_tournamentTeamsTableModel;
  private TournamentTeamsTableModel m_allTeamsTableModel;
  private TournamentOfficialsTableModel m_tournamentOfficialsTableModel;
  private TournamentOfficialsTableModel m_allOfficialsTableModel;
  private TournamentGamesTableModel m_tournamentGamesTableModel;
  private static final Integer[] ALLOWED_PLAYER_NUMBERS = { null, 1, 2, 3, 4,
      5, 6, 7, 8, 9 };

  public TournamentController(DatabaseConnection databaseConnection,
      ClassLoader classLoader) {
    m_dbConnection = databaseConnection;
    m_classLoader = classLoader;
  }

  public TournamentPanel initTournamentPanel() {
    Map<TournamentListeners, EventListener> tournamentPanelListeners = new HashMap<TournamentListeners, EventListener>();
    tournamentPanelListeners.put(TournamentListeners.TOURNAMENT_NAME,
        new TournamentNameCaretListener());
    tournamentPanelListeners.put(TournamentListeners.TOURNAMENT_LOCATION,
        new TournamentLocationCaretListener());
    tournamentPanelListeners.put(TournamentListeners.TOURNAMENT_START_DATE,
        new StartDateChangeListener());
    tournamentPanelListeners.put(TournamentListeners.TOURNAMENT_END_DATE,
        new EndDateChangeListener());
    m_tournamentPanel = new TournamentPanel(tournamentPanelListeners);
    return m_tournamentPanel;
  }

  public void enableTournamentPanel(Integer tournamentId) {
    if (tournamentId == null) {
      m_tournament = new Tournament();
      m_tournament.setStartDate(new Date());
      m_tournament.setEndDate(new Date());
      int id = m_dbConnection.insertTournament(m_tournament);
      m_tournament.setId(id);
    } else {
      m_tournament = m_dbConnection.getTournamentById(tournamentId);
    }
    m_tournamentPanel.enableAllFields();
    m_tournamentPanel.setTournamentFields(m_tournament);

    m_tournamentPanel.setTournamentDataPanel(initTournamentDataPanel());
    m_tournamentPanel.setTournamentGamesPanel(initTournamentGamesPanel());
  }

  private TournamentDataPanel initTournamentDataPanel() {
    Map<TournamentListeners, EventListener> tournamentDataPanelListeners = new HashMap<TournamentListeners, EventListener>();
    tournamentDataPanelListeners.put(TournamentListeners.TEAM_EDIT,
        new TeamEditAction());
    tournamentDataPanelListeners.put(TournamentListeners.TEAM_ADD,
        new TeamAddAction());
    tournamentDataPanelListeners.put(TournamentListeners.TEAM_REMOVE,
        new TeamRemoveAction());
    tournamentDataPanelListeners.put(TournamentListeners.OFFICIAL_ADD,
        new OfficialAddAction());
    tournamentDataPanelListeners.put(TournamentListeners.OFFICIAL_REMOVE,
        new OfficialRemoveAction());

    List<Team> tournamentTeams = m_dbConnection
        .getParticipatingTeams(m_tournament.getId());
    List<Official> tournamentOfficials = m_dbConnection
        .getParticipatingOfficials(m_tournament.getId());
    List<Team> allTeams = m_dbConnection.getAllTeams();
    List<Official> allOfficials = m_dbConnection.getAllOfficials();
    if (CollectionUtils.isNotEmpty(tournamentTeams)) {
      for (Team team : tournamentTeams) {
        allTeams.remove(team);
      }
    }
    if (CollectionUtils.isNotEmpty(tournamentOfficials)) {
      for (Official official : tournamentOfficials) {
        allOfficials.remove(official);
      }
    }
    m_tournamentTeamsTableModel = new TournamentTeamsTableModel(tournamentTeams);
    m_allTeamsTableModel = new TournamentTeamsTableModel(allTeams);
    m_tournamentOfficialsTableModel = new TournamentOfficialsTableModel(
        tournamentOfficials);
    m_allOfficialsTableModel = new TournamentOfficialsTableModel(allOfficials);

    Map<TournamentTableModels, TableModel> tournamentTableModels = new HashMap<TournamentTableModels, TableModel>();
    tournamentTableModels.put(TournamentTableModels.TEAMS_PARTICIPATING,
        m_tournamentTeamsTableModel);
    tournamentTableModels.put(TournamentTableModels.TEAMS_ALL,
        m_allTeamsTableModel);
    tournamentTableModels.put(TournamentTableModels.OFFICIALS_TOURNAMENT,
        m_tournamentOfficialsTableModel);
    tournamentTableModels.put(TournamentTableModels.OFFICIALS_ALL,
        m_allOfficialsTableModel);
    return new TournamentDataPanel(tournamentDataPanelListeners,
        tournamentTableModels);
  }

  private TournamentGamesPanel initTournamentGamesPanel() {
    Map<TournamentListeners, EventListener> tournamentGamesPanelListeners = new HashMap<TournamentListeners, EventListener>();
    tournamentGamesPanelListeners.put(TournamentListeners.GAME_ADD,
        new TournamentGameAddAction());
    tournamentGamesPanelListeners.put(TournamentListeners.GAME_EDIT,
        new TournamentGameEditAction());
    tournamentGamesPanelListeners.put(TournamentListeners.GAME_REMOVE,
        new TournamentGameRemoveAction());
    tournamentGamesPanelListeners.put(TournamentListeners.GAME_RESULTS,
        new GameResultsAction());

    List<TournamentGame> tournamentGames = m_dbConnection
        .getTournamentGames(m_tournament.getId());
    m_tournamentGamesTableModel = new TournamentGamesTableModel(tournamentGames);

    Map<TournamentTableModels, TableModel> tournamentGameTableModels = new HashMap<TournamentTableModels, TableModel>();
    tournamentGameTableModels.put(TournamentTableModels.GAMES_ALL,
        m_tournamentGamesTableModel);

    return new TournamentGamesPanel(tournamentGamesPanelListeners,
        tournamentGameTableModels);
  }

  private List<Date> calculateAvailableDates(Date startDate, Date endDate) {
    List<Date> dates = new ArrayList<Date>();
    if (startDate == null) {
      return dates;
    }

    dates.add(startDate);

    while (!DateUtils.isSameDay(startDate, endDate)) {
      startDate = DateUtils.addDays(startDate, 1);
      dates.add(startDate);
    }

    return dates;
  }

  class TournamentNameCaretListener implements CaretListener {

    @Override
    public void caretUpdate(CaretEvent e) {
      String tournamentName = ((JTextField) e.getSource()).getText();
      try {
        m_tournament.setTournamentName(tournamentName);
        m_dbConnection.updateTournament(m_tournament);
      } catch (InternalTechnicalException ex) {
        ErrorHandler.sysErr("Problem updating tournament name",
            ex.getMessage(), ex);
      } catch (Throwable t) {
        ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
      }
    }
  }

  class TournamentLocationCaretListener implements CaretListener {

    @Override
    public void caretUpdate(CaretEvent e) {
      String tournamentLocation = ((JTextField) e.getSource()).getText();
      try {
        m_tournament.setLocation(tournamentLocation);
        m_dbConnection.updateTournament(m_tournament);
      } catch (InternalTechnicalException ex) {
        ErrorHandler.sysErr("Problem updating tournament location",
            ex.getMessage(), ex);
      } catch (Throwable t) {
        ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
      }
    }
  }

  class StartDateChangeListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      Object value = evt.getNewValue();
      if (value instanceof Date) {
        Date startDate = (Date) value;
        try {
          m_tournamentPanel.setStartDate(startDate);
          m_tournament.setStartDate(startDate);
          m_dbConnection.updateTournament(m_tournament);
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem updating tournament date",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  class EndDateChangeListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      Object value = evt.getNewValue();
      if (value instanceof Date) {
        Date endDate = (Date) value;
        try {
          m_tournamentPanel.setEndDate(endDate);
          m_tournament.setEndDate(endDate);
          m_dbConnection.updateTournament(m_tournament);
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem updating tournament date",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  class TeamEditAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public TeamEditAction() {
      super("Edit");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      int selectedRow = m_tournamentPanel.getSelectedParticipatingTeamsRow();
      if (selectedRow != -1) {
        Team team = m_tournamentTeamsTableModel.getTeam(selectedRow);

        int tournamentTeamId = m_dbConnection.getTournamentTeamId(
            m_tournament.getId(), team.getId());
        List<TournamentPlayer> tournamentTeamPlayers = m_dbConnection
            .getTournamentTeamPlayers(tournamentTeamId);
        List<TournamentStaff> tournamentTeamStaff = m_dbConnection
            .getTournamentTeamStaff(tournamentTeamId);
        List<TournamentStaff> staffMembers = m_dbConnection.getStaffByTeamId(
            team.getId(), TournamentStaff.class);
        List<TournamentPlayer> players = m_dbConnection.getPlayersByTeamId(
            team.getId(), TournamentPlayer.class);
        final TournamentStaffMembersTableModel staffTableModel = new TournamentStaffMembersTableModel(
            staffMembers, tournamentTeamStaff);
        final TournamentPlayersTableModel playersTableModel = new TournamentPlayersTableModel(
            players, tournamentTeamPlayers);
        final ManageTournamentTeamPanel manageTournamentTeamPanel = new ManageTournamentTeamPanel(
            team.getTeamName(), playersTableModel, staffTableModel,
            ALLOWED_PLAYER_NUMBERS);
        final JOptionPane optionPane = new JOptionPane(
            manageTournamentTeamPanel, JOptionPane.QUESTION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION);
        final JDialog dialog = optionPane.createDialog(m_tournamentPanel,
            "Select team members for tournament");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent we) {
            // Window can only be closed by pressing OK or Cancel
          }
        });
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent e) {
            String prop = e.getPropertyName();

            if (e.getSource() == optionPane
                && prop.equals(JOptionPane.VALUE_PROPERTY)) {
              if (e.getNewValue() instanceof Integer) {
                if ((Integer) e.getNewValue() == JOptionPane.CANCEL_OPTION) {
                  dialog.setVisible(false);
                  return;
                } else if ((Integer) e.getNewValue() == JOptionPane.OK_OPTION) {
                  List<TournamentPlayer> selectedPlayers = playersTableModel
                      .getSelectedPlayers();
                  if (selectedPlayers == null || selectedPlayers.size() < 3) {
                    ErrorHandler.userErr("At least 3 players must be selected");
                    dialog.setVisible(true);
                    return;
                  }
                  List<Integer> selectedNumbers = new ArrayList<Integer>();
                  for (TournamentPlayer player : selectedPlayers) {
                    Integer playerNumber = player.getPlayerNumber();
                    if (playerNumber == null) {
                      ErrorHandler
                          .userErr("All players must have a number selected");
                      dialog.setVisible(true);
                      return;
                    }
                    if (selectedNumbers.contains(playerNumber)) {
                      ErrorHandler
                          .userErr("Two players cannot have the same number");
                      dialog.setVisible(true);
                      return;
                    }
                    selectedNumbers.add(playerNumber);
                  }
                  dialog.setVisible(false);
                }
              }
            }
          }
        });
        dialog.pack();
        dialog.setVisible(true);

        Integer value = (Integer) optionPane.getValue();
        if (value != null && value == JOptionPane.OK_OPTION) {
          try {
            m_dbConnection.deleteTournamentTeamParticipants(tournamentTeamId);
            List<TournamentPlayer> selectedPlayers = playersTableModel
                .getSelectedPlayers();
            List<TournamentStaff> selectedStaffMembers = staffTableModel
                .getSelectedStaffMembers();
            for (TournamentPlayer player : selectedPlayers) {
              m_dbConnection.insertTournamentPlayer(player.getId(),
                  tournamentTeamId, player.getPlayerNumber());
            }
            for (TournamentStaff staffMember : selectedStaffMembers) {
              m_dbConnection.insertTournamentStaff(staffMember.getId(),
                  tournamentTeamId);
            }
          } catch (InternalTechnicalException ex) {
            ErrorHandler.sysErr("Problem adding team to tournament",
                ex.getMessage(), ex);
          } catch (Throwable t) {
            ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
          }
        }
      }
    }
  }

  @SuppressWarnings("serial")
  class TeamAddAction extends AbstractAction {

    public TeamAddAction() {
      super("<-");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      int selectedRow = m_tournamentPanel.getSelectedAllTeamsRow();
      if (selectedRow != -1) {
        Team team = m_allTeamsTableModel.getTeam(selectedRow);
        List<TournamentStaff> staffMembers = m_dbConnection.getStaffByTeamId(
            team.getId(), TournamentStaff.class);
        List<TournamentPlayer> players = m_dbConnection.getPlayersByTeamId(
            team.getId(), TournamentPlayer.class);
        final TournamentStaffMembersTableModel staffTableModel = new TournamentStaffMembersTableModel(
            staffMembers);
        final TournamentPlayersTableModel playersTableModel = new TournamentPlayersTableModel(
            players);
        final ManageTournamentTeamPanel manageTournamentTeamPanel = new ManageTournamentTeamPanel(
            team.getTeamName(), playersTableModel, staffTableModel,
            ALLOWED_PLAYER_NUMBERS);
        final JOptionPane optionPane = new JOptionPane(
            manageTournamentTeamPanel, JOptionPane.QUESTION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION);
        final JDialog dialog = optionPane.createDialog(m_tournamentPanel,
            "Select team members for tournament");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent we) {
            // Window can only be closed by pressing OK or Cancel
          }
        });
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent e) {
            String prop = e.getPropertyName();

            if (e.getSource() == optionPane
                && prop.equals(JOptionPane.VALUE_PROPERTY)) {
              if (e.getNewValue() instanceof Integer) {
                if ((Integer) e.getNewValue() == JOptionPane.CANCEL_OPTION) {
                  dialog.setVisible(false);
                  return;
                } else if ((Integer) e.getNewValue() == JOptionPane.OK_OPTION) {
                  List<TournamentPlayer> selectedPlayers = playersTableModel
                      .getSelectedPlayers();
                  if (selectedPlayers == null || selectedPlayers.size() < 3) {
                    ErrorHandler.userErr("At least 3 players must be selected");
                    dialog.setVisible(true);
                    return;
                  }
                  List<Integer> selectedNumbers = new ArrayList<Integer>();
                  for (TournamentPlayer player : selectedPlayers) {
                    Integer playerNumber = player.getPlayerNumber();
                    if (playerNumber == null) {
                      ErrorHandler
                          .userErr("All players must have a number selected");
                      dialog.setVisible(true);
                      return;
                    }
                    if (selectedNumbers.contains(playerNumber)) {
                      ErrorHandler
                          .userErr("Two players cannot have the same number");
                      dialog.setVisible(true);
                      return;
                    }
                    selectedNumbers.add(playerNumber);
                  }
                  dialog.setVisible(false);
                }
              }
            }
          }
        });
        dialog.pack();
        dialog.setVisible(true);

        Integer value = (Integer) optionPane.getValue();
        if (value != null && value == JOptionPane.OK_OPTION) {
          try {
            List<TournamentPlayer> selectedPlayers = playersTableModel
                .getSelectedPlayers();
            List<TournamentStaff> selectedStaffMembers = staffTableModel
                .getSelectedStaffMembers();
            Integer tournamentTeamId = m_dbConnection.addTeamToTournament(
                m_tournament.getId(), team.getId());
            for (TournamentPlayer player : selectedPlayers) {
              m_dbConnection.insertTournamentPlayer(player.getId(),
                  tournamentTeamId, player.getPlayerNumber());
            }
            for (TournamentStaff staffMember : selectedStaffMembers) {
              m_dbConnection.insertTournamentStaff(staffMember.getId(),
                  tournamentTeamId);
            }
            m_allTeamsTableModel.removeTeam(team, selectedRow);
            m_tournamentTeamsTableModel.addTeam(team);
          } catch (InternalTechnicalException ex) {
            ErrorHandler.sysErr("Problem adding team to tournament",
                ex.getMessage(), ex);
          } catch (Throwable t) {
            ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
          }
        }
      }
    }
  }

  @SuppressWarnings("serial")
  class TeamRemoveAction extends AbstractAction {
    public TeamRemoveAction() {
      super("->");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      int selectedRow = m_tournamentPanel.getSelectedParticipatingTeamsRow();
      if (selectedRow != -1) {
        Team team = m_tournamentTeamsTableModel.getTeam(selectedRow);
        Integer tournamentTeamId = m_dbConnection.getTournamentTeamId(
            m_tournament.getId(), team.getId());
        try {
          m_dbConnection.deleteTournamentTeamData(tournamentTeamId);
          m_tournamentTeamsTableModel.removeTeam(team, selectedRow);
          m_allTeamsTableModel.addTeam(team);
        } catch (InternalIntegrityConstraintException ex) {
          ErrorHandler.userWrn(ex.getMessage());
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem deleting team from tournament",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  @SuppressWarnings("serial")
  class OfficialAddAction extends AbstractAction {
    public OfficialAddAction() {
      super("<-");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      int selectedRow = m_tournamentPanel.getSelectedAllOfficialsRow();
      if (selectedRow != -1) {
        try {
          Official official = m_allOfficialsTableModel.getOfficial(selectedRow);
          m_dbConnection.addOfficialToTournament(m_tournament.getId(),
              official.getId());
          m_allOfficialsTableModel.removeOfficial(official, selectedRow);
          m_tournamentOfficialsTableModel.addOfficial(official);
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem adding official to tournament",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  @SuppressWarnings("serial")
  class OfficialRemoveAction extends AbstractAction {
    public OfficialRemoveAction() {
      super("->");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      int selectedRow = m_tournamentPanel.getSelectedTournamentOfficialsRow();
      if (selectedRow != -1) {
        Official official = m_tournamentOfficialsTableModel
            .getOfficial(selectedRow);
        try {
          m_dbConnection.deleteOfficialFromTournament(m_tournament.getId(),
              official.getId());
          m_tournamentOfficialsTableModel.removeOfficial(official, selectedRow);
          m_allOfficialsTableModel.addOfficial(official);
        } catch (InternalIntegrityConstraintException ex) {
          ErrorHandler.userWrn(ex.getMessage());
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem deleting official from tournament",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  @SuppressWarnings("serial")
  class TournamentGameAddAction extends AbstractAction {
    private TournamentGame tournamentGame;

    public TournamentGameAddAction() {
      super("Add game");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      tournamentGame = new TournamentGame();
      tournamentGame.setTournament(m_tournament);
      String defaultVenue = m_tournament.getLocation();
      Map<TournamentGameListeners, EventListener> tournamentGameDataListeners = new HashMap<TournamentGameListeners, EventListener>();
      tournamentGameDataListeners.put(TournamentGameListeners.GAME_NUMBER,
          new TournamentGameNumberListener(tournamentGame));
      tournamentGameDataListeners.put(TournamentGameListeners.TEAM_A,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEAM_A));
      tournamentGameDataListeners.put(TournamentGameListeners.TEAM_B,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEAM_B));
      tournamentGameDataListeners.put(TournamentGameListeners.DATE,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.DATE));
      tournamentGameDataListeners.put(TournamentGameListeners.REFEREE_1,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.REFEREE_1));
      tournamentGameDataListeners.put(TournamentGameListeners.REFEREE_2,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.REFEREE_2));
      tournamentGameDataListeners.put(TournamentGameListeners.TEN_SEC_1,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEN_SEC_1));
      tournamentGameDataListeners.put(TournamentGameListeners.TEN_SEC_2,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEN_SEC_2));
      tournamentGameDataListeners.put(TournamentGameListeners.SCORER,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.SCORER));
      tournamentGameDataListeners.put(TournamentGameListeners.TIMER,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TIMER));
      tournamentGameDataListeners.put(TournamentGameListeners.BACKUP_TIMER,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.BACKUP_TIMER));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_1,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_1));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_2,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_2));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_3,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_3));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_4,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_4));
      tournamentGameDataListeners.put(TournamentGameListeners.TIME,
          new TournamentGameCaretListener(tournamentGame,
              TournamentGameTextType.TIME));
      tournamentGameDataListeners.put(TournamentGameListeners.POOL,
          new TournamentGameCaretListener(tournamentGame,
              TournamentGameTextType.POOL));
      tournamentGameDataListeners.put(TournamentGameListeners.VENUE,
          new TournamentGameCaretListener(tournamentGame,
              TournamentGameTextType.VENUE));
      tournamentGameDataListeners.put(TournamentGameListeners.GENDER,
          new TournamentGameGenderSelectionListener(tournamentGame));
      final TournamentGameDataPanel gameDataPanel = new TournamentGameDataPanel(
          tournamentGameDataListeners,
          m_dbConnection.getTournamentTeams(m_tournament.getId()),
          m_dbConnection.getTournamentOfficials(m_tournament.getId()),
          calculateAvailableDates(m_tournament.getStartDate(),
              m_tournament.getEndDate()), defaultVenue);
      tournamentGame.setVenue(defaultVenue);
      gameDataPanel.selectDefaultData();
      final JOptionPane optionPane = new JOptionPane(gameDataPanel,
          JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog dialog = optionPane.createDialog(gameDataPanel, "Add game");
      dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

      dialog.setModal(true);
      dialog.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
          // Window can only be closed by pressing OK or Cancel
        }
      });

      optionPane.addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e) {
          String prop = e.getPropertyName();

          if (e.getSource() == optionPane
              && prop.equals(JOptionPane.VALUE_PROPERTY)) {
            if (e.getNewValue() instanceof Integer) {
              if ((Integer) e.getNewValue() == JOptionPane.CANCEL_OPTION) {
                dialog.setVisible(false);
                return;
              } else if ((Integer) e.getNewValue() == JOptionPane.OK_OPTION) {
                if (!tournamentGame.hasValidData()) {
                  ErrorHandler.userErr("All required data must be selected");
                  dialog.setVisible(true);
                  return;
                }
                dialog.setVisible(false);
              }
            }
          }
        }
      });

      dialog.pack();
      dialog.setVisible(true);

      Integer value = (Integer) optionPane.getValue();
      if (value != null && value == JOptionPane.OK_OPTION) {
        try {
          Integer tournamentGameId = m_dbConnection
              .insertTournamentGame(tournamentGame);
          tournamentGame.setId(tournamentGameId);
          m_tournamentGamesTableModel.addTournamentGame(tournamentGame);
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem adding tournament game",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  @SuppressWarnings("serial")
  class TournamentGameEditAction extends AbstractAction {
    private TournamentGame tournamentGame;

    public TournamentGameEditAction() {
      super("Edit game");
    }

    @Override
    public void actionPerformed(ActionEvent event) {

      int selectedRow = m_tournamentPanel.getSelectedGamesRow();
      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(m_tournamentPanel,
            "Please select a game", "No row selected",
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      // tournamentGame = m_tournamentGamesTableModel
      // .removeTournamentGame(selectedRow);
      tournamentGame = m_tournamentGamesTableModel
          .getTournamentGame(selectedRow);

      Map<TournamentGameListeners, EventListener> tournamentGameDataListeners = new HashMap<TournamentGameListeners, EventListener>();
      tournamentGameDataListeners.put(TournamentGameListeners.GAME_NUMBER,
          new TournamentGameNumberListener(tournamentGame));
      tournamentGameDataListeners.put(TournamentGameListeners.TEAM_A,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEAM_A));
      tournamentGameDataListeners.put(TournamentGameListeners.TEAM_B,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEAM_B));
      tournamentGameDataListeners.put(TournamentGameListeners.DATE,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.DATE));
      tournamentGameDataListeners.put(TournamentGameListeners.REFEREE_1,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.REFEREE_1));
      tournamentGameDataListeners.put(TournamentGameListeners.REFEREE_2,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.REFEREE_2));
      tournamentGameDataListeners.put(TournamentGameListeners.TEN_SEC_1,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEN_SEC_1));
      tournamentGameDataListeners.put(TournamentGameListeners.TEN_SEC_2,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TEN_SEC_2));
      tournamentGameDataListeners.put(TournamentGameListeners.SCORER,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.SCORER));
      tournamentGameDataListeners.put(TournamentGameListeners.TIMER,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.TIMER));
      tournamentGameDataListeners.put(TournamentGameListeners.BACKUP_TIMER,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.BACKUP_TIMER));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_1,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_1));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_2,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_2));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_3,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_3));
      tournamentGameDataListeners.put(TournamentGameListeners.GOAL_JUDGE_4,
          new TournamentGameComboBoxListener(tournamentGame,
              TournamentGameComboBoxType.GOAL_JUDGE_4));
      tournamentGameDataListeners.put(TournamentGameListeners.TIME,
          new TournamentGameCaretListener(tournamentGame,
              TournamentGameTextType.TIME));
      tournamentGameDataListeners.put(TournamentGameListeners.POOL,
          new TournamentGameCaretListener(tournamentGame,
              TournamentGameTextType.POOL));
      tournamentGameDataListeners.put(TournamentGameListeners.VENUE,
          new TournamentGameCaretListener(tournamentGame,
              TournamentGameTextType.VENUE));
      tournamentGameDataListeners.put(TournamentGameListeners.GENDER,
          new TournamentGameGenderSelectionListener(tournamentGame));
      tournamentGameDataListeners.put(TournamentGameListeners.NEEDS_WINNER,
          new TournamentGameNeedsWinnerSelectionListener(tournamentGame));
      final TournamentGameDataPanel gameDataPanel = new TournamentGameDataPanel(
          tournamentGameDataListeners,
          m_dbConnection.getTournamentTeams(m_tournament.getId()),
          m_dbConnection.getTournamentOfficials(m_tournament.getId()),
          calculateAvailableDates(m_tournament.getStartDate(),
              m_tournament.getEndDate()), null);
      gameDataPanel.setGameData(tournamentGame);
      final JOptionPane optionPane = new JOptionPane(gameDataPanel,
          JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog dialog = optionPane.createDialog(gameDataPanel, "Edit");
      dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

      dialog.setModal(true);
      dialog.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
          // Window can only be closed by pressing OK or Cancel
        }
      });

      optionPane.addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e) {
          String prop = e.getPropertyName();

          if (e.getSource() == optionPane
              && prop.equals(JOptionPane.VALUE_PROPERTY)) {
            if (e.getNewValue() instanceof Integer) {
              if ((Integer) e.getNewValue() == JOptionPane.CANCEL_OPTION) {
                dialog.setVisible(false);
                return;
              } else if ((Integer) e.getNewValue() == JOptionPane.OK_OPTION) {
                if (!tournamentGame.hasValidData()) {
                  ErrorHandler.userErr("All required data must be selected");
                  dialog.setVisible(true);
                  return;
                }
                dialog.setVisible(false);
              }
            }
          }
        }
      });

      dialog.pack();
      dialog.setVisible(true);

      Integer value = (Integer) optionPane.getValue();
      if (value != null) {
        if (value == JOptionPane.OK_OPTION) {
          try {
            m_dbConnection.updateTournamentGame(tournamentGame);
            m_tournamentGamesTableModel.fireTableRowsUpdated(selectedRow,
                selectedRow);
          } catch (InternalTechnicalException ex) {
            ErrorHandler.sysErr("Problem adding tournament game",
                ex.getMessage(), ex);
          } catch (Throwable t) {
            ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
          }
        } else if (value == JOptionPane.CANCEL_OPTION) {
          tournamentGame = m_dbConnection.getTournamentGame(tournamentGame
              .getId());
        }
      }
    }
  }

  @SuppressWarnings("serial")
  class TournamentGameRemoveAction extends AbstractAction {
    public TournamentGameRemoveAction() {
      super("Remove game");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      int selectedRow = m_tournamentPanel.getSelectedGamesRow();
      if (selectedRow != -1) {
        int selection = JOptionPane.showConfirmDialog(m_tournamentPanel,
            "Are you sure you want to delete the selected game?",
            "Delete player", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if (selection == JOptionPane.YES_OPTION) {
          TournamentGame tournamentGame = m_tournamentGamesTableModel
              .getTournamentGame(selectedRow);
          try {
            m_dbConnection.deleteTournamentGameById(tournamentGame.getId());
            m_tournamentGamesTableModel.removeTournamentGame(tournamentGame,
                selectedRow);
          } catch (InternalTechnicalException ex) {
            ErrorHandler.sysErr("Problem deleting game from database",
                ex.getMessage(), ex);
          } catch (Throwable t) {
            ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
          }
        }
      } else {
        JOptionPane.showMessageDialog(m_tournamentPanel,
            "Please select a game", "No row selected",
            JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  class TournamentGameCaretListener implements CaretListener {
    private TournamentGame tournamentGame;
    private TournamentGameTextType textType;

    public TournamentGameCaretListener(TournamentGame tournamentGame,
        TournamentGameTextType textType) {
      this.tournamentGame = tournamentGame;
      this.textType = textType;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
      if (e.getSource() instanceof JTextField) {
        JTextField source = (JTextField) e.getSource();
        String text = source.getText();
        switch (textType) {
        case TIME:
          tournamentGame.setGameTime(text);
          break;
        case POOL:
          tournamentGame.setPool(text);
          break;
        case VENUE:
          tournamentGame.setVenue(text);
          break;
        }
      }
    }
  }

  class TournamentGameGenderSelectionListener implements ActionListener {
    private TournamentGame tournamentGame;

    public TournamentGameGenderSelectionListener(TournamentGame tournamentGame) {
      this.tournamentGame = tournamentGame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      tournamentGame.setGender(e.getActionCommand());
    }
  }

  class TournamentGameNeedsWinnerSelectionListener implements ItemListener {
    private TournamentGame tournamentGame;

    public TournamentGameNeedsWinnerSelectionListener(
        TournamentGame tournamentGame) {
      this.tournamentGame = tournamentGame;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
      if (e.getStateChange() == ItemEvent.SELECTED) {
        tournamentGame.setNeedsWinner(true);
      } else {
        tournamentGame.setNeedsWinner(false);
      }
    }

  }

  class TournamentGameNumberListener implements ChangeListener {

    private TournamentGame tournamentGame;

    public TournamentGameNumberListener(TournamentGame tournamentGame) {
      this.tournamentGame = tournamentGame;
    }

    @Override
    public void stateChanged(ChangeEvent event) {
      if (event != null && event.getSource() != null
          && event.getSource() instanceof JSpinner) {
        JSpinner source = (JSpinner) event.getSource();
        if (source.getModel() instanceof SpinnerNumberModel) {
          SpinnerNumberModel model = (SpinnerNumberModel) source.getModel();
          tournamentGame.setGameNumber(model.getNumber().intValue());
        }
      }
    }
  }

  class TournamentGameComboBoxListener implements ActionListener {
    private TournamentGame tournamentGame;
    private TournamentGameComboBoxType comboBoxType;

    public TournamentGameComboBoxListener(TournamentGame tournamentGame,
        TournamentGameComboBoxType comboBoxType) {
      this.tournamentGame = tournamentGame;
      this.comboBoxType = comboBoxType;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void actionPerformed(ActionEvent event) {
      Object source = event.getSource();
      if (source instanceof JComboBox) {
        JComboBox sourceCB = (JComboBox) source;
        Object selectedItem = sourceCB.getSelectedItem();
        if (selectedItem instanceof Date) {
          tournamentGame.setGameDate((Date) selectedItem);
        } else if (selectedItem instanceof TournamentTeam) {
          TournamentTeam selectedTeam = (TournamentTeam) selectedItem;
          switch (comboBoxType) {
          case TEAM_A:
            tournamentGame.setTeamA(selectedTeam);
            break;
          case TEAM_B:
            tournamentGame.setTeamB(selectedTeam);
            break;
          }
        } else if (selectedItem instanceof TournamentOfficial) {
          TournamentOfficial official = (TournamentOfficial) selectedItem;
          switch (comboBoxType) {
          case REFEREE_1:
            tournamentGame.setReferee1(official);
            break;
          case REFEREE_2:
            tournamentGame.setReferee2(official);
            break;
          case TEN_SEC_1:
            tournamentGame.setTenSeconds1(official);
            break;
          case TEN_SEC_2:
            tournamentGame.setTenSeconds2(official);
            break;
          case SCORER:
            tournamentGame.setScorer(official);
            break;
          case TIMER:
            tournamentGame.setTimer(official);
            break;
          case BACKUP_TIMER:
            tournamentGame.setBackupTimer(official);
            break;
          case GOAL_JUDGE_1:
            tournamentGame.setGoalJudge1(official);
            break;
          case GOAL_JUDGE_2:
            tournamentGame.setGoalJudge2(official);
            break;
          case GOAL_JUDGE_3:
            tournamentGame.setGoalJudge3(official);
            break;
          case GOAL_JUDGE_4:
            tournamentGame.setGoalJudge4(official);
            break;
          }
        }
      }
    }
  }

  public void createAllScoreSheets() {
    int numberOfGames = m_tournamentPanel.getNumberOfGames();

    final JFileChooser fileChooser = new JFileChooser(m_gameSheetsSaveDir);
    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int option = fileChooser.showOpenDialog(m_tournamentPanel);

    if (option == JFileChooser.APPROVE_OPTION) {
      File directory = fileChooser.getSelectedFile();
      String directoryPath = directory.getAbsolutePath();

      for (int i = 0; i < numberOfGames; i++) {
        TournamentGame tournamentGame = m_tournamentGamesTableModel
            .getTournamentGame(i);
        String defaultFileName = tournamentGame.getDefaultScoreSheetFileName();

        File scoreSheetFile = new File(directoryPath, defaultFileName + ".pdf");
        createScoreSheet(tournamentGame, scoreSheetFile);
      }
    }
  }

  public void createScoreSheet() {
    int selectedRow = m_tournamentPanel.getSelectedGamesRow();
    if (selectedRow != -1) {
      try {
        createScoreSheet(
            m_tournamentGamesTableModel.getTournamentGame(selectedRow), null);

      } catch (InternalTechnicalException ex) {
        ErrorHandler
            .sysErr("Problem creating score sheet", ex.getMessage(), ex);
      } catch (Throwable t) {
        ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
      }
    } else {
      JOptionPane.showMessageDialog(m_tournamentPanel, "Please select a game",
          "No row selected", JOptionPane.WARNING_MESSAGE);
    }
  }

  private void createScoreSheet(final TournamentGame tournamentGame,
      File scoreSheetFile) {
    final InputStream scoreSheetTemplate = m_classLoader
        .getResourceAsStream("data/ScoreSheetRegulationTime.pdf");
    final List<TournamentPlayer> teamAPlayers = m_dbConnection
        .getTournamentTeamPlayers(tournamentGame.getTeamA().getId());
    final List<TournamentStaff> teamAStaff = m_dbConnection
        .getTournamentTeamStaff(tournamentGame.getTeamA().getId());
    final List<TournamentPlayer> teamBPlayers = m_dbConnection
        .getTournamentTeamPlayers(tournamentGame.getTeamB().getId());
    final List<TournamentStaff> teamBStaff = m_dbConnection
        .getTournamentTeamStaff(tournamentGame.getTeamB().getId());

    final String fileSuffix = "pdf";
    final JFileChooser fileChooser = new JFileChooser(m_gameSheetsSaveDir);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

    final Map<String, String> gameInformationMap = PdfFieldConverter
        .createGameInformationMap(tournamentGame, teamAPlayers, teamAStaff,
            teamBPlayers, teamBStaff);

    if (scoreSheetFile == null) {
      String defaultFileName = tournamentGame.getDefaultScoreSheetFileName();
      fileChooser.setFileFilter(new FileNameExtensionFilter("PDF file",
          fileSuffix));
      fileChooser.setSelectedFile(new File(defaultFileName));
      fileChooser.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          if (evt.getActionCommand().startsWith("Approve")) {
            File destFile;
            m_gameSheetsSaveDir = fileChooser.getCurrentDirectory();
            String fileName = fileChooser.getSelectedFile().toString();

            if (fileName.endsWith("." + fileSuffix)) {
              destFile = new File(fileName);
            } else {
              destFile = new File(fileName + "." + fileSuffix);
            }

            if (destFile.exists()) {
              int selectedInd = JOptionPane.showConfirmDialog(
                  m_tournamentPanel, "File allready exists!\n"
                      + "Overwrite file?", "File exists",
                  JOptionPane.OK_CANCEL_OPTION);

              if (selectedInd == JOptionPane.OK_OPTION) {
                PdfUtil.createPdfFromTemplate(gameInformationMap,
                    scoreSheetTemplate, destFile);
                if (tournamentGame.getNeedsWinner()) {
                  createOvertimeScoreeSheets(gameInformationMap,
                      destFile.getParentFile(), destFile.getName());
                }
              }
            } else {
              // if file is not found create and save it!
              PdfUtil.createPdfFromTemplate(gameInformationMap,
                  scoreSheetTemplate, destFile);
              if (tournamentGame.getNeedsWinner()) {
                createOvertimeScoreeSheets(gameInformationMap,
                    destFile.getParentFile(), destFile.getName());
              }
            }
          }
        }
      });

      fileChooser.showSaveDialog(m_tournamentPanel);
    } else {
      PdfUtil.createPdfFromTemplate(gameInformationMap, scoreSheetTemplate,
          scoreSheetFile);
      if (tournamentGame.getNeedsWinner()) {
        createOvertimeScoreeSheets(gameInformationMap,
            scoreSheetFile.getParentFile(), scoreSheetFile.getName());
      }
    }
  }

  private void createOvertimeScoreeSheets(
      Map<String, String> gameInformationMap, File parent, String baseFileName) {

    final String PDF_SUFFIX = ".pdf";

    String overtimeSheetFileName;
    String lineUpSheetTeamAFileName;
    String lineUpSheetTeamBFileName;
    if (baseFileName.endsWith(PDF_SUFFIX)) {
      // Remove .pdf
      baseFileName = baseFileName.substring(0, baseFileName.length() - 4);
    }
    overtimeSheetFileName = baseFileName + "_overtime" + PDF_SUFFIX;
    lineUpSheetTeamAFileName = baseFileName + "_line_up_"
        + gameInformationMap.get(PdfFieldConstants.FIELD_TEAM_A_SHORT_NAME)
        + PDF_SUFFIX;
    lineUpSheetTeamBFileName = baseFileName + "_line_up_"
        + gameInformationMap.get(PdfFieldConstants.FIELD_TEAM_B_SHORT_NAME)
        + PDF_SUFFIX;

    final InputStream overtimeSheetTemplate = m_classLoader
        .getResourceAsStream("data/ScoreSheetOvertime.pdf");
    final InputStream lineUpSheetTeamATemplate = m_classLoader
        .getResourceAsStream("data/LineUpSheet.pdf");
    final InputStream lineUpSheetTeamBTemplate = m_classLoader
        .getResourceAsStream("data/LineUpSheet.pdf");

    PdfUtil.createPdfFromTemplate(gameInformationMap, overtimeSheetTemplate,
        new File(parent, overtimeSheetFileName));
    PdfUtil.createPdfFromTemplate(gameInformationMap, lineUpSheetTeamATemplate,
        new File(parent, lineUpSheetTeamAFileName));
    gameInformationMap.put(FIELD_TEAM_A_NAME,
        gameInformationMap.get(FIELD_TEAM_B_NAME));
    PdfUtil.createPdfFromTemplate(gameInformationMap, lineUpSheetTeamBTemplate,
        new File(parent, lineUpSheetTeamBFileName));
  }

  public void createTournamentStats() {
    final List<TournamentStats> tournamentStats = m_dbConnection
        .getTournamentRefStats(m_tournament.getId());

    final String fileSuffix = "pdf";
    final JFileChooser fileChooser = new JFileChooser(m_tournamentStatsSaveDir);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
    fileChooser.setFileFilter(new FileNameExtensionFilter("PDF file",
        fileSuffix));
    StringBuilder defaultFileName = new StringBuilder();
    defaultFileName.append("Tournament Stats");
    fileChooser.setSelectedFile(new File(defaultFileName.toString()));
    fileChooser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().startsWith("Approve")) {
          File destFile;
          m_tournamentStatsSaveDir = fileChooser.getCurrentDirectory();
          String fileName = fileChooser.getSelectedFile().toString();

          if (fileName.endsWith("." + fileSuffix)) {
            destFile = new File(fileName);
          } else {
            destFile = new File(fileName + "." + fileSuffix);
          }

          if (destFile.exists()) {
            int selectedInd = JOptionPane.showConfirmDialog(m_tournamentPanel,
                "File allready exists!\n" + "Overwrite file?", "File exists",
                JOptionPane.OK_CANCEL_OPTION);

            if (selectedInd == JOptionPane.OK_OPTION) {
              PdfUtil.createTournamentStats(destFile, tournamentStats);
            }
          } else {
            // if file is not found create and save it!
            PdfUtil.createTournamentStats(destFile, tournamentStats);
          }
        }
      }
    });

    fileChooser.showSaveDialog(m_tournamentPanel);
  }

  class GameResultsAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public GameResultsAction() {
      super("Enter game results");
    }

    private boolean validatePlayerNumbers(String[] input,
        List<Integer> validNumbers) {
      for (String value : input) {
        if (StringUtils.isBlank(value)) {
          if (input.length == 1) {
            return true;
          }
          return false;
        }
        try {
          int intValue = Integer.parseInt(value);
          if (!validNumbers.contains(intValue)) {
            if (intValue == 0) {
              // Always accept 0 as an own goal
              return true;
            }
            return false;
          }
        } catch (NumberFormatException e) {
          return false;
        }
      }

      return true;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      int selectedRow = m_tournamentPanel.getSelectedGamesRow();
      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(m_tournamentPanel,
            "Please select a game", "No row selected",
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      TournamentGame tournamentGame = m_tournamentGamesTableModel
          .getTournamentGame(selectedRow);
      final List<Integer> teamAPlayers = m_dbConnection
          .getTournamentTeamPlayerNumbers(tournamentGame.getTeamA().getId());
      final List<Integer> teamBPlayers = m_dbConnection
          .getTournamentTeamPlayerNumbers(tournamentGame.getTeamB().getId());
      final GameResultsPanel gameResultsPanel = new GameResultsPanel(
          tournamentGame.getTeamA().getDisplayName(), tournamentGame.getTeamB()
              .getDisplayName());
      final JOptionPane optionPane = new JOptionPane(gameResultsPanel,
          JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog dialog = optionPane.createDialog(m_tournamentPanel,
          "Enter game results");
      dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

      dialog.setModal(true);
      dialog.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
          // Window can only be closed by pressing OK or Cancel
        }
      });
      optionPane.addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e) {
          String prop = e.getPropertyName();

          if (e.getSource() == optionPane
              && prop.equals(JOptionPane.VALUE_PROPERTY)) {
            if (e.getNewValue() instanceof Integer
                && (Integer) e.getNewValue() == JOptionPane.CANCEL_OPTION) {
              dialog.setVisible(false);
              return;
            }
            String[] teamAScorers = gameResultsPanel.getScorersTeamA().trim()
                .split("\\s+");
            String[] teamBScorers = gameResultsPanel.getScorersTeamB().trim()
                .split("\\s+");
            if (validatePlayerNumbers(teamAScorers, teamAPlayers)
                && validatePlayerNumbers(teamBScorers, teamBPlayers)) {
              gameResultsPanel.setErrorText(null);
              dialog.setVisible(false);
              return;
            }

            // Input not valid
            gameResultsPanel
                .setErrorText("Enter number of scorers separated by spaces");
            dialog.setVisible(true);
          }
        }
      });
      dialog.pack();
      dialog.setVisible(true);

      Integer value = (Integer) optionPane.getValue();
      if (value != null && value == JOptionPane.OK_OPTION) {
        try {
          String[] scorersTeamA = gameResultsPanel.getScorersTeamA().trim()
              .split("\\s+");
          String[] scorersTeamB = gameResultsPanel.getScorersTeamB().trim()
              .split("\\s+");
          int finalScoreTeamA = scorersTeamA.length == 1 ? (StringUtils
              .isBlank(scorersTeamA[0]) ? 0 : 1) : scorersTeamA.length;
          int finalScoreTeamB = scorersTeamB.length == 1 ? (StringUtils
              .isBlank(scorersTeamB[0]) ? 0 : 1) : scorersTeamB.length;

          StringBuilder resultString = new StringBuilder();
          resultString.append(tournamentGame.getTeamA().getDisplayName());
          resultString.append(" : ");
          resultString.append(tournamentGame.getTeamB().getDisplayName());
          resultString.append("\n");
          resultString.append(finalScoreTeamA);
          resultString.append(" : ");
          resultString.append(finalScoreTeamB);

          int confirm = JOptionPane.showConfirmDialog(gameResultsPanel,
              resultString.toString(), "Confirm game results",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

          if (confirm == JOptionPane.OK_OPTION) {
            Map<Integer, Integer> goalsTeamA = new HashMap<Integer, Integer>();
            Map<Integer, Integer> goalsTeamB = new HashMap<Integer, Integer>();

            for (String scorerTeamA : scorersTeamA) {
              if (StringUtils.isBlank(scorerTeamA) || scorerTeamA.equals("0")) {
                continue;
              }
              Integer playerNumber = Integer.parseInt(scorerTeamA);
              if (goalsTeamA.containsKey(playerNumber)) {
                goalsTeamA.put(playerNumber, goalsTeamA.get(playerNumber) + 1);
              } else {
                goalsTeamA.put(playerNumber, 1);
              }
            }
            for (String scorerTeamB : scorersTeamB) {
              if (StringUtils.isBlank(scorerTeamB) || scorerTeamB.equals("0")) {
                continue;
              }
              Integer playerNumber = Integer.parseInt(scorerTeamB);
              if (goalsTeamB.containsKey(playerNumber)) {
                goalsTeamB.put(playerNumber, goalsTeamB.get(playerNumber) + 1);
              } else {
                goalsTeamB.put(playerNumber, 1);
              }
            }

            m_dbConnection.setGameScore(tournamentGame.getId(),
                finalScoreTeamA, finalScoreTeamB);
            m_dbConnection.setGameScorers(tournamentGame.getId(),
                tournamentGame.getTeamA().getId(), tournamentGame.getTeamB()
                    .getId(), goalsTeamA, goalsTeamB);
            tournamentGame.setScoreTeamA(finalScoreTeamA);
            tournamentGame.setScoreTeamB(finalScoreTeamB);
            m_tournamentGamesTableModel.fireTableRowsUpdated(selectedRow,
                selectedRow);
          }
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem updating staff member in database.",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  public void createTournamentResults() {
    List<TournamentGame> tournamentGames = m_dbConnection
        .getTournamentGames(m_tournament.getId());
    Iterator<TournamentGame> iter = tournamentGames.iterator();

    final Map<String, Map<TournamentTeam, GameResult>> endResults = new HashMap<String, Map<TournamentTeam, GameResult>>();
    final Map<String, List<TournamentGame>> endGames = new HashMap<String, List<TournamentGame>>();

    while (iter.hasNext()) {
      TournamentGame game = iter.next();
      TournamentTeam teamA = game.getTeamA();
      TournamentTeam teamB = game.getTeamB();
      Integer scoreTeamA = game.getScoreTeamA();
      Integer scoreTeamB = game.getScoreTeamB();

      if (scoreTeamA == null || scoreTeamB == null) {
        continue;
      }

      boolean needsWinner = game.getNeedsWinner();
      String pool = game.getPool();
      if (!needsWinner && StringUtils.isBlank(pool)) {
        pool = GameResult.GENERAL_GAME;
      }

      Map<TournamentTeam, GameResult> results;
      if (endResults.containsKey(pool)) {
        results = endResults.get(pool);
        List<TournamentGame> games = endGames.get(pool);
        games.add(game);
      } else {
        results = new HashMap<TournamentTeam, GameResult>();
        endResults.put(pool, results);
        List<TournamentGame> games = new ArrayList<TournamentGame>();
        games.add(game);
        endGames.put(pool, games);
      }

      if (results.containsKey(teamA)) {
        GameResult gameResult = results.get(teamA);
        handleResults(gameResult, scoreTeamA, scoreTeamB);
      } else {
        GameResult result = new GameResult(teamA.getDisplayName());
        handleResults(result, scoreTeamA, scoreTeamB);
        results.put(teamA, result);
      }

      if (results.containsKey(teamB)) {
        GameResult gameResult = results.get(teamB);
        handleResults(gameResult, scoreTeamB, scoreTeamA);
      } else {
        GameResult result = new GameResult(teamB.getDisplayName());
        handleResults(result, scoreTeamB, scoreTeamA);
        results.put(teamB, result);
      }
    }

    final Map<String, List<GameResult>> finalResults = new HashMap<String, List<GameResult>>();
    for (String key : endResults.keySet()) {
      finalResults.put(key, new ArrayList<GameResult>(endResults.get(key)
          .values()));
    }
    final String fileSuffix = "pdf";
    final JFileChooser fileChooser = new JFileChooser(
        m_tournamentResultsSaveDir);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
    fileChooser.setFileFilter(new FileNameExtensionFilter("PDF file",
        fileSuffix));
    StringBuilder defaultFileName = new StringBuilder();
    defaultFileName.append("Tournament results");
    fileChooser.setSelectedFile(new File(defaultFileName.toString()));
    fileChooser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().startsWith("Approve")) {
          File destFile;
          m_tournamentResultsSaveDir = fileChooser.getCurrentDirectory();
          String fileName = fileChooser.getSelectedFile().toString();

          if (fileName.endsWith("." + fileSuffix)) {
            destFile = new File(fileName);
          } else {
            destFile = new File(fileName + "." + fileSuffix);
          }

          if (destFile.exists()) {
            int selectedInd = JOptionPane.showConfirmDialog(m_tournamentPanel,
                "File allready exists!\n" + "Overwrite file?", "File exists",
                JOptionPane.OK_CANCEL_OPTION);

            if (selectedInd == JOptionPane.OK_OPTION) {
              PdfUtil.createTournamentResults(destFile, finalResults, endGames,
                  m_dbConnection.getTournamentScorers(m_tournament.getId()));
            }
          } else {
            // if file is not found create and save it!
            PdfUtil.createTournamentResults(destFile, finalResults, endGames,
                m_dbConnection.getTournamentScorers(m_tournament.getId()));
          }
        }
      }
    });

    fileChooser.showSaveDialog(m_tournamentPanel);

  }

  private void handleResults(GameResult result, Integer scoreTeam,
      Integer scoreOther) {
    if (scoreTeam > scoreOther) {
      result.addWin();
    } else if (scoreTeam == scoreOther) {
      result.addDraw();
    } else {
      result.addLoss();
    }

    result.addGoalsScored(scoreTeam);
    result.addGoalsConceded(scoreOther);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void createRefereeSchedule() {
    List<String> tournamentVenues = m_dbConnection
        .getTournamentVenues(m_tournament.getId());
    try {
      final RefereeSchedulePanel venuePanel = new RefereeSchedulePanel(
          new DefaultComboBoxModel(tournamentVenues.toArray()));
      final JOptionPane venueOptionPane = new JOptionPane(venuePanel,
          JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog venueDialog = venueOptionPane.createDialog("Select venue");
      venueDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

      venueDialog.setModal(true);
      venueDialog.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
          // Window can only be closed by pressing OK or Cancel
        }
      });
      venueOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e) {
          String prop = e.getPropertyName();

          if (e.getSource() == venueOptionPane
              && prop.equals(JOptionPane.VALUE_PROPERTY)) {
            if (e.getNewValue() instanceof Integer
                && (Integer) e.getNewValue() == JOptionPane.CANCEL_OPTION) {
              venueDialog.setVisible(false);
              return;
            }
            venueDialog.setVisible(false);
          }
        }
      });
      venueDialog.pack();
      venueDialog.setVisible(true);

      Integer value = (Integer) venueOptionPane.getValue();
      if (value == null || value != JOptionPane.OK_OPTION) {
        return;
      }
      final String venue = venuePanel.getVenue();
      final Integer fromGame = venuePanel.getFromGame();
      final Integer toGame = venuePanel.getToGame();

      final List<TournamentGame> tournamentGames = m_dbConnection
          .getTournamentGamesByVenue(m_tournament.getId(), venue, fromGame,
              toGame);
      final String fileSuffix = "pdf";
      final JFileChooser fileChooser = new JFileChooser(m_gameSheetsSaveDir);
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
      fileChooser.setFileFilter(new FileNameExtensionFilter("PDF file",
          fileSuffix));
      StringBuilder defaultFileName = new StringBuilder();
      defaultFileName.append("Referees schedule ");
      defaultFileName.append(venue);
      fileChooser.setSelectedFile(new File(defaultFileName.toString()));
      fileChooser.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          if (evt.getActionCommand().startsWith("Approve")) {
            File destFile;
            m_gameSheetsSaveDir = fileChooser.getCurrentDirectory();
            String fileName = fileChooser.getSelectedFile().toString();

            if (fileName.endsWith("." + fileSuffix)) {
              destFile = new File(fileName);
            } else {
              destFile = new File(fileName + "." + fileSuffix);
            }

            if (destFile.exists()) {
              int selectedInd = JOptionPane.showConfirmDialog(
                  m_tournamentPanel, "File allready exists!\n"
                      + "Overwrite file?", "File exists",
                  JOptionPane.OK_CANCEL_OPTION);

              if (selectedInd == JOptionPane.OK_OPTION) {
                PdfUtil
                    .createRefereesSchedule(destFile, tournamentGames, venue);
              }
            } else {
              // if file is not found create and save it!
              PdfUtil.createRefereesSchedule(destFile, tournamentGames, venue);
            }
          }
        }
      });

      fileChooser.showSaveDialog(m_tournamentPanel);

    } catch (InternalTechnicalException ex) {
      ErrorHandler.sysErr("Problem creating score sheet", ex.getMessage(), ex);
    } catch (Throwable t) {
      ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
    }
  }
}
