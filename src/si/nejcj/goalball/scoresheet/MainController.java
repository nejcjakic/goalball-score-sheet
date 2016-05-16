package si.nejcj.goalball.scoresheet;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang3.BooleanUtils;
import org.xml.sax.SAXException;

import si.nejcj.goalball.scoresheet.admin.AdminController;
import si.nejcj.goalball.scoresheet.db.DatabaseConnection;
import si.nejcj.goalball.scoresheet.db.entity.Official;
import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;
import si.nejcj.goalball.scoresheet.db.entity.Player;
import si.nejcj.goalball.scoresheet.db.entity.Staff;
import si.nejcj.goalball.scoresheet.db.entity.Team;
import si.nejcj.goalball.scoresheet.db.entity.Tournament;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;
import si.nejcj.goalball.scoresheet.tournament.TournamentController;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentDataTableModel;
import si.nejcj.goalball.scoresheet.tournament.panel.SelectTournamentPanel;
import si.nejcj.goalball.scoresheet.tournament.panel.TournamentPanel;
import si.nejcj.goalball.scoresheet.util.Constants;
import si.nejcj.goalball.scoresheet.util.ErrorHandler;
import si.nejcj.goalball.scoresheet.util.UpdateHandler;
import si.nejcj.goalball.scoresheet.util.panel.HtmlPanel;

public class MainController {
  // This should be changed in the build.xml file and is changed here
  // automatically
  public static final String VERSION = "1.0";

  private MainFrame m_mainFrame;
  private TournamentPanel m_tournamentPanel;

  protected ClassLoader m_classLoader;

  protected DatabaseConnection m_dbConnection;
  private AdminController m_adminController;
  private TournamentController m_tournamentController;

  /**
   * Initiates the main controller, creates the required GUI components and
   * reads the program configuration from the properties file if it exists.
   */
  public MainController() {
    try {
      m_classLoader = this.getClass().getClassLoader();

      m_mainFrame = new MainFrame();
      m_mainFrame.setIconImage(new ImageIcon(
          m_classLoader.getResource("images/64x64/Scoresheet.png")).getImage());

      initDatabase();
      initData();

      m_adminController = new AdminController(m_dbConnection, m_classLoader);
      m_tournamentController = new TournamentController(m_dbConnection,
          m_classLoader);
      createMenuBar();

      m_mainFrame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent arg0) {
          saveOnClose();
        }
      });

      showWhatsNewDialog();
      m_tournamentPanel = m_tournamentController.initTournamentPanel();
      m_mainFrame.add(m_tournamentPanel);
      m_mainFrame.pack();
      m_mainFrame.setVisible(true);
    } catch (Throwable t) {
      // Catch all exception handling to not let any errors get to user
      ErrorHandler.sysErr(
          "Unexpected error was encountered in application. GoalballScoreSheet will shut down.",
          t.getMessage(), t);
      saveOnClose();
      System.exit(1);
    }
  }

  private void initDatabase() {
    try {
      m_dbConnection = new DatabaseConnection(
          System.getProperty(Constants.USER_HOME_PROPERTY_NAME));
    } catch (InternalTechnicalException e) {
      ErrorHandler.sysErr("Problem loading the database", e.getMessage(), e);
    }
  }

  private void initData() {
    String firstRun = m_dbConnection
        .getConfiguration(Constants.PROPERTY_FIRST_RUN);
    if (firstRun == null || BooleanUtils.toBoolean(firstRun)) {
      InputStream xmlData = m_classLoader
          .getResourceAsStream("data/initialData.xml");
      InputStream validationXmlData = m_classLoader
          .getResourceAsStream("data/initialData.xml");
      updateAppData(validationXmlData, xmlData);
      m_dbConnection.updateOrAddConfiguration(Constants.PROPERTY_FIRST_RUN,
          "false");
    }
  }

  private void showWhatsNewDialog() {
    boolean isShowDialog = true;
    try {
      String versionForDialog = m_dbConnection
          .getConfiguration(Constants.PROPERTY_LAST_VERSION);
      if (versionForDialog != null && versionForDialog.equals(VERSION)) {
        isShowDialog = false;
      }
      if (isShowDialog) {
        URL content = m_classLoader.getResource("help/whatsnew.html");
        JCheckBox showNextTime = new JCheckBox("Show this message at startup");
        showNextTime.setSelected(true);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setPage(content);
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        JPanel contentPanel = new JPanel();
        contentPanel.add(scrollPane);

        JOptionPane.showMessageDialog(m_mainFrame,
            new Object[] { contentPanel, showNextTime,
                new JSeparator(JSeparator.HORIZONTAL) },
            "GoalballScoreSheet version " + VERSION,
            JOptionPane.INFORMATION_MESSAGE);
        if (!showNextTime.isSelected()) {
          m_dbConnection.updateOrAddConfiguration(
              Constants.PROPERTY_LAST_VERSION, VERSION);
        }
      }
    } catch (Exception ex) {
      ErrorHandler.sysErr(
          "Can not access version info for 'What's new' dialog ",
          ex.getMessage(), ex);
    }
  }

  private void saveOnClose() {
    try {
      m_dbConnection.close();
    } catch (Throwable e) {
      ErrorHandler.sysErr("Problem closing the database", e.getMessage(), e);
    }
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    m_mainFrame.setJMenuBar(menuBar);

    // File menu
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    JMenuItem newTournament = new JMenuItem(new MbNewTournamentAction());
    JMenuItem openTournament = new JMenuItem(new MbOpenTournamentAction());
    JMenuItem removeTournament = new JMenuItem(new MbRemoveTournamentAction());
    JMenuItem quit = new JMenuItem(new MbQuitAction());
    fileMenu.add(newTournament);
    fileMenu.add(openTournament);
    fileMenu.add(removeTournament);
    fileMenu.addSeparator();
    fileMenu.add(quit);
    menuBar.add(fileMenu);

    // Tools menu
    JMenu toolsMenu = new JMenu("Tools");
    toolsMenu.setMnemonic(KeyEvent.VK_T);
    JMenuItem manageAppData = new JMenuItem(new MbManageAppDataAction());
    JMenuItem updateDbData = new JMenuItem(new MbUpdateDatabaseDataAction());
    toolsMenu.add(manageAppData);
    toolsMenu.add(updateDbData);
    menuBar.add(toolsMenu);

    // Tournament menu
    JMenu tournamentMenu = new JMenu("Tournament");
    JMenuItem scoreSheetItem = new JMenuItem(new CreateScoreSheetAction());
    JMenuItem allScoreSheetsItem = new JMenuItem(
        new CreateAllScoreSheetsAction());
    JMenuItem teamScheduleItem = new JMenuItem(new CreateTeamScheduleAction());
    JMenuItem refScheduleItem = new JMenuItem(
        new CreateRefereeScheduleAction());
    JMenuItem teamDisplayNamesItem = new JMenuItem(
        new CreateTeamDisplayNamesAction());
    JMenuItem teamLineUpSheetsItem = new JMenuItem(
        new CreateLineUpSheetsAction());
    JMenuItem resultInputTableItem = new JMenuItem(
        new CreateResultsInputTable());
    JMenuItem tournamentResultsItem = new JMenuItem(
        new CreateTournamentResultsAction());
    JMenuItem tournamentStatsItem = new JMenuItem(
        new CreateTournamentStatsAction());
    JMenuItem refereeSheetsItem = new JMenuItem(
        new CreateRefereeGamesSheetsAction());
    tournamentMenu.add(scoreSheetItem);
    tournamentMenu.add(allScoreSheetsItem);
    tournamentMenu.add(teamScheduleItem);
    tournamentMenu.add(refScheduleItem);
    tournamentMenu.add(teamLineUpSheetsItem);
    tournamentMenu.add(teamDisplayNamesItem);
    tournamentMenu.add(resultInputTableItem);
    tournamentMenu.add(tournamentResultsItem);
    tournamentMenu.add(tournamentStatsItem);
    tournamentMenu.add(refereeSheetsItem);
    menuBar.add(tournamentMenu);

    // Help menu
    JMenu helpMenu = new JMenu("Help");
    JMenuItem about = new JMenuItem(new MbHelpAboutAction());
    helpMenu.add(about);
    JMenuItem contents = new JMenuItem(new MbHelpContentsAction());
    helpMenu.add(contents);
    menuBar.add(helpMenu);
  }

  @SuppressWarnings("serial")
  class MbNewTournamentAction extends AbstractAction {
    public MbNewTournamentAction() {
      putValue(Action.NAME, "New tournament");
      putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
      putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
      putValue(Action.SMALL_ICON,
          new ImageIcon(m_classLoader.getResource("images/16x16/New.png")));
    }

    public void actionPerformed(ActionEvent event) {
      m_tournamentController.enableTournamentPanel(null);
    }
  }

  @SuppressWarnings("serial")
  class MbOpenTournamentAction extends AbstractAction {
    public MbOpenTournamentAction() {
      putValue(Action.NAME, "Open tournament");
      putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
      putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
      putValue(Action.SMALL_ICON,
          new ImageIcon(m_classLoader.getResource("images/16x16/Open.png")));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      List<Tournament> tournaments = m_dbConnection.getAllTournaments();
      final TournamentDataTableModel tournamentDataTableModel = new TournamentDataTableModel(
          tournaments);
      final SelectTournamentPanel selectTournamentPanel = new SelectTournamentPanel(
          tournamentDataTableModel);
      final JOptionPane optionPane = new JOptionPane(selectTournamentPanel,
          JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog dialog = optionPane.createDialog(m_tournamentPanel,
          "Select tournament");
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
                if (selectTournamentPanel.getSelectedRow() == -1) {
                  ErrorHandler.userErr("Please select a tournament");
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
        Tournament selectedTournament = tournamentDataTableModel
            .getTournament(selectTournamentPanel.getSelectedRow());
        m_tournamentController
            .enableTournamentPanel(selectedTournament.getId());
      }
    }
  }

  @SuppressWarnings("serial")
  class MbRemoveTournamentAction extends AbstractAction {
    public MbRemoveTournamentAction() {
      putValue(Action.NAME, "Remove tournament");
      putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
      putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
      putValue(Action.SMALL_ICON,
          new ImageIcon(m_classLoader.getResource("images/16x16/Delete.png")));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      List<Tournament> tournaments = m_dbConnection.getAllTournaments();
      final TournamentDataTableModel tournamentDataTableModel = new TournamentDataTableModel(
          tournaments);
      final SelectTournamentPanel selectTournamentPanel = new SelectTournamentPanel(
          tournamentDataTableModel);
      final JOptionPane optionPane = new JOptionPane(selectTournamentPanel,
          JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog dialog = optionPane.createDialog(m_tournamentPanel,
          "Select tournament to delete");
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
                if (selectTournamentPanel.getSelectedRow() == -1) {
                  ErrorHandler.userErr("Please select a tournament to delete");
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
        Tournament selectedTournament = tournamentDataTableModel
            .getTournament(selectTournamentPanel.getSelectedRow());
        m_dbConnection.deleteTournamentData(selectedTournament.getId());
        // m_tournamentController.enableTournamentPanel(null);
        m_mainFrame.remove(m_tournamentPanel);
        m_tournamentPanel = m_tournamentController.initTournamentPanel();
        m_mainFrame.add(m_tournamentPanel);
        m_mainFrame.pack();
      }
    }
  }

  @SuppressWarnings("serial")
  class MbQuitAction extends AbstractAction {

    public MbQuitAction() {
      putValue(Action.NAME, "Quit");
      putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
      putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      saveOnClose();
      System.exit(0);
    }
  }

  class CreateLineUpSheetsAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public CreateLineUpSheetsAction() {
      super("Create team line up sheets");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createTournamentLineUp();
    }
  }

  class CreateScoreSheetAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public CreateScoreSheetAction() {
      super("Create score sheet");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createScoreSheet();
    }

  }

  class CreateAllScoreSheetsAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public CreateAllScoreSheetsAction() {
      super("Create all score sheets");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createAllScoreSheets();
    }
  }

  class CreateTeamScheduleAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public CreateTeamScheduleAction() {
      super("Create team schedule");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createTeamSchedule();
    }
  }

  class CreateRefereeScheduleAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public CreateRefereeScheduleAction() {
      super("Create referee schedule");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createRefereeSchedule();
    }
  }

  class CreateTeamDisplayNamesAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public CreateTeamDisplayNamesAction() {
      super("Generate team display names");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createTeamDisplayNames();
    }
  }

  class CreateResultsInputTable extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public CreateResultsInputTable() {
      super("Generate results input table");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createResultInputTable();
    }
  }

  class CreateTournamentResultsAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public CreateTournamentResultsAction() {
      super("Generate tournament results");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createTournamentResults();
    }
  }

  class CreateTournamentStatsAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public CreateTournamentStatsAction() {
      super("Create tournament statistics");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createTournamentStats();
    }
  }

  class CreateRefereeGamesSheetsAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public CreateRefereeGamesSheetsAction() {
      super("Create referee games sheets");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      m_tournamentController.createRefereeGamesSheets();
      ;
    }
  }

  @SuppressWarnings("serial")
  class MbHelpAboutAction extends AbstractAction {

    public MbHelpAboutAction() {
      super("About");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      JOptionPane.showMessageDialog(null,
          new HtmlPanel("help/about.html", 300, 200), "About",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  @SuppressWarnings("serial")
  class MbHelpContentsAction extends AbstractAction {
    public MbHelpContentsAction() {
      super("Contents");
    }

    public void actionPerformed(ActionEvent arg0) {
      JOptionPane.showMessageDialog(null,
          new HtmlPanel("help/contents.html", 600, 350), "Contents",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  @SuppressWarnings("serial")
  class MbManageAppDataAction extends AbstractAction {

    public MbManageAppDataAction() {
      putValue(Action.NAME, "Manage application data");
      putValue(Action.MNEMONIC_KEY, KeyEvent.VK_M);
      putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
      putValue(Action.SMALL_ICON,
          new ImageIcon(m_classLoader.getResource("images/16x16/Manage.png")));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      m_adminController.displayManageAppDataPanel();
    }
  }

  @SuppressWarnings("serial")
  class MbUpdateDatabaseDataAction extends AbstractAction {
    public MbUpdateDatabaseDataAction() {
      super("Update Database Data");
      putValue(Action.SMALL_ICON,
          new ImageIcon(m_classLoader.getResource("images/16x16/Update.png")));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      // TODO: Data should be obtained from update page
      // InputStream xmlData = m_classLoader
      // .getResourceAsStream("data/initialData.xml");
      // InputStream validationXmlData = m_classLoader
      // .getResourceAsStream("data/initialData.xml");
      // updateAppData(validationXmlData, xmlData);
    }
  }

  @Deprecated
  private void updateAppData(InputStream validationXmlData,
      InputStream xmlData) {
    try {
      SchemaFactory factory = SchemaFactory
          .newInstance("http://www.w3.org/2001/XMLSchema");
      Source schemaLocation = new StreamSource(
          m_classLoader.getResourceAsStream("data/dataUpdateSchema.xsd"));
      Schema schema = factory.newSchema(schemaLocation);
      Validator validator = schema.newValidator();
      Source source = new StreamSource(validationXmlData);
      validator.validate(source);

      UpdateHandler handler = new UpdateHandler();
      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
      parser.parse(xmlData, handler);

      // TODO: Admin data cannot be deleted due to tournament constraints
      m_dbConnection.deleteAdminData();
      for (Official official : handler.getOfficials()) {
        OfficialLevel level = m_dbConnection
            .getOfficialLevelByName(official.getOfficialLevel().getLevelName());
        official.setOfficialLevel(level);
        official.setAdminData(true);
        m_dbConnection.insertOfficial(official);
      }

      Map<Team, List<Staff>> teamStaff = handler.getTeamStaff();

      for (Team team : teamStaff.keySet()) {
        Team dbTeam = m_dbConnection.getTeamByNameAndCountry(team.getTeamName(),
            team.getCountry());
        if (dbTeam == null) {
          team.setAdminData(true);
          Integer teamId = m_dbConnection.insertTeam(team);
          dbTeam = team;
          dbTeam.setId(teamId);
        }

        List<Staff> staff = teamStaff.get(team);
        for (Staff staffMember : staff) {
          staffMember.setAdminData(true);
          m_dbConnection.insertStaffMember(staffMember, dbTeam.getId());
        }
      }

      Map<Team, List<Player>> teamPlayers = handler.getTeamPlayers();
      for (Team team : teamPlayers.keySet()) {
        Team dbTeam = m_dbConnection.getTeamByNameAndCountry(team.getTeamName(),
            team.getCountry());
        if (dbTeam == null) {
          team.setAdminData(true);
          Integer teamId = m_dbConnection.insertTeam(team);
          dbTeam = team;
          dbTeam.setId(teamId);
        }

        List<Player> players = teamPlayers.get(team);
        for (Player player : players) {
          player.setAdminData(true);
          m_dbConnection.insertPlayer(player, dbTeam.getId());
        }
      }
    } catch (ParserConfigurationException e) {
      ErrorHandler.sysErr("Error updating application data", e.getMessage(), e);
    } catch (SAXException e) {
      ErrorHandler.sysErr("Error updating application data", e.getMessage(), e);
    } catch (IOException e) {
      ErrorHandler.sysErr("Error updating application data", e.getMessage(), e);
    }
  }
}
