package si.nejcj.goalball.scoresheet.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import si.nejcj.goalball.scoresheet.admin.model.OfficialLevelsComboBoxModel;
import si.nejcj.goalball.scoresheet.admin.model.OfficialsTableModel;
import si.nejcj.goalball.scoresheet.admin.model.PlayersTableModel;
import si.nejcj.goalball.scoresheet.admin.model.StaffMembersTableModel;
import si.nejcj.goalball.scoresheet.admin.model.TeamNamesComboBoxModel;
import si.nejcj.goalball.scoresheet.admin.panel.AddTeamPanel;
import si.nejcj.goalball.scoresheet.admin.panel.ManageAppDataPanel;
import si.nejcj.goalball.scoresheet.admin.panel.ManageOfficialsDataPanel;
import si.nejcj.goalball.scoresheet.admin.panel.ManageTeamDataPanel;
import si.nejcj.goalball.scoresheet.db.DatabaseConnection;
import si.nejcj.goalball.scoresheet.db.DatabaseUtil;
import si.nejcj.goalball.scoresheet.db.entity.Official;
import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;
import si.nejcj.goalball.scoresheet.db.entity.Player;
import si.nejcj.goalball.scoresheet.db.entity.Staff;
import si.nejcj.goalball.scoresheet.db.entity.Team;
import si.nejcj.goalball.scoresheet.exception.business.InternalIntegrityConstraintException;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;
import si.nejcj.goalball.scoresheet.util.Constants.ManageTeamDataActions;
import si.nejcj.goalball.scoresheet.util.ErrorHandler;
import si.nejcj.goalball.scoresheet.util.SwingAction;

public class AdminController {

  private DatabaseConnection m_dbConnection;
  private ClassLoader m_classLoader;

  private ManageTeamDataPanel manageTeamDataPanel;
  private TeamNamesComboBoxModel teamNamesModel;
  private StaffMembersTableModel staffMembersTableModel;
  private PlayersTableModel playersTableModel;

  private ManageOfficialsDataPanel manageOfficialsDataPanel;
  private OfficialsTableModel officialsTableModel;
  private OfficialLevelsComboBoxModel officialLevelsComboBoxModel;

  public AdminController(DatabaseConnection databaseConnection,
      ClassLoader classLoader) {
    m_dbConnection = databaseConnection;
    m_classLoader = classLoader;
  }

  public void displayManageAppDataPanel() {
    ManageAppDataPanel manageAppDataPanel = new ManageAppDataPanel();
    manageAppDataPanel.addTab("Manage Teams",
        new ImageIcon(m_classLoader.getResource("images/64x64/Teams.png")),
        initManageTeamsPanel(),
        "Manage global data about teams (staff and players)");
    manageAppDataPanel.addTab("Manage Officials",
        new ImageIcon(m_classLoader.getResource("images/64x64/Whistle.png")),
        initManageOfficialsPanel(), "Manage global data about officials");
    JOptionPane.showMessageDialog(null, manageAppDataPanel,
        "Manage application data", JOptionPane.INFORMATION_MESSAGE);
  }

  private JPanel initManageTeamsPanel() {
    List<Team> teams = m_dbConnection.getAllTeams();

    teamNamesModel = new TeamNamesComboBoxModel(teams);
    Map<ManageTeamDataActions, AbstractAction> manageTeamDataPanelActions = new HashMap<ManageTeamDataActions, AbstractAction>();
    manageTeamDataPanelActions.put(ManageTeamDataActions.ADD_TEAM_ACTION,
        new AddTeamAction());
    manageTeamDataPanelActions.put(ManageTeamDataActions.ADD_STAFF_ACTION,
        SwingAction.of("Add staff member", e -> addStaff()));
    manageTeamDataPanelActions.put(ManageTeamDataActions.REMOVE_STAFF_ACTION,
        SwingAction.of("Delete staff member", e -> removeStaff()));
    manageTeamDataPanelActions.put(ManageTeamDataActions.ADD_PLAYER_ACTION,
        SwingAction.of("Add player", e -> addPlayer()));
    manageTeamDataPanelActions.put(ManageTeamDataActions.REMOVE_PLAYER_ACTION,
        SwingAction.of("Delete player", e -> deletePlayer()));
    manageTeamDataPanel = new ManageTeamDataPanel(manageTeamDataPanelActions);
    manageTeamDataPanel.init(teamNamesModel, new TeamSelectionListener());

    return manageTeamDataPanel;
  }

  private JPanel initManageOfficialsPanel() {
    List<Official> officials = m_dbConnection.getAllOfficials();
    officialsTableModel = new OfficialsTableModel(officials);
    List<OfficialLevel> officialLevels = m_dbConnection.getAllOfficialLevels();
    officialLevelsComboBoxModel = new OfficialLevelsComboBoxModel(
        officialLevels);
    manageOfficialsDataPanel = new ManageOfficialsDataPanel(officialsTableModel,
        officialLevelsComboBoxModel, new OfficialsTableModelListener(),
        SwingAction.of("Add official", e -> addOfficial()),
        SwingAction.of("Delete official", e -> deleteOfficial()));
    return manageOfficialsDataPanel;
  }

  class TeamSelectionListener implements ActionListener {

    @SuppressWarnings("rawtypes")
    @Override
    public void actionPerformed(ActionEvent event) {
      Object source = event.getSource();
      if (source instanceof JComboBox) {
        JComboBox sourceCB = (JComboBox) source;
        Object selectedItem = sourceCB.getSelectedItem();
        if (selectedItem instanceof Team) {
          Team selectedTeam = (Team) selectedItem;
          try {
            staffMembersTableModel = new StaffMembersTableModel(m_dbConnection
                .getStaffByTeamId(selectedTeam.getId(), Staff.class));
            manageTeamDataPanel.setStaffTableModel(staffMembersTableModel,
                new StaffTableModelListener());
            staffMembersTableModel.fireTableDataChanged();

            playersTableModel = new PlayersTableModel(m_dbConnection
                .getPlayersByTeamId(selectedTeam.getId(), Player.class));
            manageTeamDataPanel.setPlayersTableModel(playersTableModel,
                new PlayersTableModelListener());
            playersTableModel.fireTableDataChanged();
          } catch (InternalTechnicalException e) {
            ErrorHandler.sysErr("Problem retrieving data", e.getMessage(), e);
          } catch (Throwable t) {
            ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
          }
        }
      }
    }
  }

  class OfficialsTableModelListener implements TableModelListener {

    @Override
    public void tableChanged(TableModelEvent event) {
      if (event.getColumn() == -1) {
        return;
      }
      int row = event.getFirstRow();
      Official official = officialsTableModel.getOfficial(row);
      if (official != null) {
        try {
          Official dbOfficial = m_dbConnection
              .getOfficialById(official.getId());
          if (!official.hasEqualData(dbOfficial)) {
            m_dbConnection.updateOfficial(official);
          }
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem updating official in database.",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  private void addOfficial() {
    Official official = new Official();
    try {
      int officialId = m_dbConnection.insertOfficial(official);
      official.setId(officialId);
      officialsTableModel.addOfficial(official);
    } catch (InternalTechnicalException ex) {
      ErrorHandler.sysErr("Problem adding official to database",
          ex.getMessage(), ex);
    } catch (Throwable t) {
      ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
    }
  }

  private void deleteOfficial() {
    int row = manageOfficialsDataPanel.getSelectedRow();
    if (row != -1) {
      Official official = officialsTableModel.getOfficial(row);
      int selection = JOptionPane.showConfirmDialog(manageOfficialsDataPanel,
          "Are you sure you want to delete the selected official?",
          "Delete official", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
      if (selection == JOptionPane.YES_OPTION) {
        try {
          m_dbConnection.deleteOfficialById(official.getId());
          officialsTableModel.removeOfficial(official, row);
        } catch (InternalIntegrityConstraintException ex) {
          ErrorHandler.userWrn(ex.getMessage());
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem deleting official from database",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    } else {
      JOptionPane.showMessageDialog(manageOfficialsDataPanel,
          "Please select an official", "No row selected",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  class StaffTableModelListener implements TableModelListener {

    @Override
    public void tableChanged(TableModelEvent e) {
      if (e.getColumn() == -1) {
        return;
      }
      int row = e.getFirstRow();
      Staff staffMember = staffMembersTableModel.getStaffMember(row);
      if (staffMember != null) {
        try {
          Staff dbStaffMember = m_dbConnection
              .getStaffById(staffMember.getId());
          if (!staffMember.hasEqualData(dbStaffMember)) {
            m_dbConnection.updateStaffMember(staffMember);
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

  @SuppressWarnings("serial")
  class AddTeamAction extends AbstractAction {
    private List<String> countries;

    public AddTeamAction() {
      super("Add team");
      countries = DatabaseUtil.getListOfCountries();

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void actionPerformed(ActionEvent event) {
      final AddTeamPanel addTeamPanel = new AddTeamPanel(
          new DefaultComboBoxModel(countries.toArray()));
      final JOptionPane optionPane = new JOptionPane(addTeamPanel,
          JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog dialog = optionPane.createDialog(manageTeamDataPanel,
          "Add team");
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
            if (!addTeamPanel.isInputValid()) {
              dialog.setVisible(true);
            } else {
              dialog.setVisible(false);
            }
          }
        }
      });
      dialog.pack();
      dialog.setVisible(true);

      Integer value = (Integer) optionPane.getValue();
      if (value != null && value == JOptionPane.OK_OPTION) {
        try {
          Team team = addTeamPanel.getTeam();
          Integer teamId = m_dbConnection.insertTeam(team);
          team.setId(teamId);
          teamNamesModel.addTeam(team);
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem updating staff member in database.",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  private void addStaff() {
    Staff staffMember = new Staff();
    try {
      int staffId = m_dbConnection.insertStaffMember(staffMember,
          ((Team) teamNamesModel.getSelectedItem()).getId());
      staffMember.setId(staffId);
      staffMembersTableModel.addStaffMember(staffMember);
    } catch (InternalTechnicalException ex) {
      ErrorHandler.sysErr("Problem adding staff member to database",
          ex.getMessage(), ex);
    } catch (Throwable t) {
      ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
    }
  }

  public void removeStaff() {
    int row = manageTeamDataPanel.getSelectedStaffRow();
    if (row != -1) {
      Staff staffMember = staffMembersTableModel.getStaffMember(row);
      int selection = JOptionPane.showConfirmDialog(manageTeamDataPanel,
          "Are you sure you want to delete the selected staff member?",
          "Delete staff member", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
      if (selection == JOptionPane.YES_OPTION) {
        try {
          m_dbConnection.deleteStaffMemberById(staffMember.getId());
          staffMembersTableModel.removeStaffMember(staffMember, row);
        } catch (InternalIntegrityConstraintException ex) {
          ErrorHandler.userWrn(ex.getMessage());
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem deleting staff member from database",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    } else {
      JOptionPane.showMessageDialog(manageTeamDataPanel,
          "Please select a staff member", "No row selected",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  class PlayersTableModelListener implements TableModelListener {

    @Override
    public void tableChanged(TableModelEvent e) {
      if (e.getColumn() == -1) {
        return;
      }
      int row = e.getFirstRow();
      Player player = playersTableModel.getPlayer(row);
      if (player != null) {
        try {
          Player dbPlayer = m_dbConnection.getPlayerById(player.getId());
          if (!player.hasEqualData(dbPlayer)) {
            m_dbConnection.updatePlayer(player);
          }
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem updating player in database.",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    }
  }

  public void addPlayer() {
    Player player = new Player();
    try {
      int playerId = m_dbConnection.insertPlayer(player,
          ((Team) teamNamesModel.getSelectedItem()).getId());
      player.setId(playerId);
      playersTableModel.addPlayer(player);
    } catch (InternalTechnicalException ex) {
      ErrorHandler.sysErr("Problem adding player to database", ex.getMessage(),
          ex);
    } catch (Throwable t) {
      ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
    }
  }

  public void deletePlayer() {
    int row = manageTeamDataPanel.getSelectedPlayerRow();
    if (row != -1) {
      Player player = playersTableModel.getPlayer(row);
      int selection = JOptionPane.showConfirmDialog(manageTeamDataPanel,
          "Are you sure you want to delete the selected player?",
          "Delete player", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
      if (selection == JOptionPane.YES_OPTION) {
        try {
          m_dbConnection.deletePlayerById(player.getId());
          playersTableModel.removePlayer(player, row);
        } catch (InternalIntegrityConstraintException ex) {
          ErrorHandler.userWrn(ex.getMessage());
        } catch (InternalTechnicalException ex) {
          ErrorHandler.sysErr("Problem deleting player from database",
              ex.getMessage(), ex);
        } catch (Throwable t) {
          ErrorHandler.sysErr("Unexpected error", t.getMessage(), t);
        }
      }
    } else {
      JOptionPane.showMessageDialog(manageTeamDataPanel,
          "Please select a player", "No row selected",
          JOptionPane.WARNING_MESSAGE);
    }
  }
}
