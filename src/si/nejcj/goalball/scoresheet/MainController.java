package si.nejcj.goalball.scoresheet;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;

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

import si.nejcj.goalball.scoresheet.admin.AdminController;
import si.nejcj.goalball.scoresheet.db.DatabaseConnection;
import si.nejcj.goalball.scoresheet.db.entity.Tournament;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;
import si.nejcj.goalball.scoresheet.tournament.TournamentController;
import si.nejcj.goalball.scoresheet.tournament.model.TournamentDataTableModel;
import si.nejcj.goalball.scoresheet.tournament.panel.SelectTournamentPanel;
import si.nejcj.goalball.scoresheet.tournament.panel.TournamentPanel;
import si.nejcj.goalball.scoresheet.util.Constants;
import si.nejcj.goalball.scoresheet.util.ErrorHandler;
import si.nejcj.goalball.scoresheet.util.SwingAction;
import si.nejcj.goalball.scoresheet.util.panel.HtmlPanel;
import si.nejcj.goalball.scoresheet.util.pdf.TournamentDataUtil;
import si.nejcj.goalball.scoresheet.util.pdf.TournamentRefereesUtil;

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
    toolsMenu.add(manageAppData);
    menuBar.add(toolsMenu);

    // Reports menu
    JMenu reportsMenu = new JMenu("Reports");

    // Referees menu items
    JMenu refereesMenu = new JMenu("Referees");
    JMenuItem refScheduleItem = new JMenuItem(
        SwingAction.of("Create referee schedule",
            e -> m_tournamentController.createRefereeSchedule()));
    JMenuItem refereeSheetsItem = new JMenuItem(
        SwingAction.of("Create referee games sheets",
            e -> m_tournamentController.createRefereeGamesSheets()));
    JMenuItem refereeStatsItem = new JMenuItem(
        SwingAction.of("Create referee statistics",
            e -> m_tournamentController.createRefereeStats()));
    // TODO: This is WC Malmo specific stats
    JMenuItem createRefStatsAction = new JMenuItem(
        SwingAction.of("Create ref stats", e -> TournamentRefereesUtil
            .createRefStats(m_dbConnection.getTournamentGames(7))));
    refereesMenu.add(refScheduleItem);
    refereesMenu.add(refereeSheetsItem);
    refereesMenu.add(refereeStatsItem);
    refereesMenu.add(createRefStatsAction);

    // Games menu items
    JMenu gamesMenu = new JMenu("Games");
    JMenuItem scoreSheetItem = new JMenuItem(SwingAction.of(
        "Create score sheet", e -> m_tournamentController.createScoreSheet()));
    JMenuItem allScoreSheetsItem = new JMenuItem(
        SwingAction.of("Create all score sheets",
            e -> m_tournamentController.createAllScoreSheets()));
    JMenuItem teamScheduleItem = new JMenuItem(
        SwingAction.of("Create team schedule",
            e -> m_tournamentController.createTeamSchedule()));
    JMenuItem teamDisplayNamesItem = new JMenuItem(
        SwingAction.of("Generate team display names",
            e -> m_tournamentController.createTeamDisplayNames()));
    JMenuItem teamLineUpSheetsItem = new JMenuItem(
        SwingAction.of("Create team line up sheets",
            e -> m_tournamentController.createTournamentLineUp()));
    // TODO: Open dialog to select file and tournament - maybe propagate to
    // tournament controller?
    JMenuItem createTeamStatisticsAction = new JMenuItem(
        SwingAction.of("Create team game statistics", e -> TournamentDataUtil
            .createTeamStatistics(null, m_dbConnection.getTournamentGames(7))));
    gamesMenu.add(scoreSheetItem);
    gamesMenu.add(allScoreSheetsItem);
    gamesMenu.add(teamScheduleItem);
    gamesMenu.add(teamDisplayNamesItem);
    gamesMenu.add(teamLineUpSheetsItem);
    gamesMenu.add(createTeamStatisticsAction);

    // Results menu
    JMenu resultsMenu = new JMenu("Results");
    JMenuItem resultInputTableItem = new JMenuItem(
        SwingAction.of("Generate results input table",
            e -> m_tournamentController.createResultInputTable()));
    JMenuItem tournamentResultsItem = new JMenuItem(
        SwingAction.of("Generate tournament results",
            e -> m_tournamentController.createTournamentResults()));
    resultsMenu.add(resultInputTableItem);
    resultsMenu.add(tournamentResultsItem);

    reportsMenu.add(refereesMenu);
    reportsMenu.add(gamesMenu);
    reportsMenu.add(resultsMenu);
    menuBar.add(reportsMenu);

    // Help menu
    JMenu helpMenu = new JMenu("Help");
    JMenuItem about = new JMenuItem(SwingAction.of("About",
        e -> JOptionPane.showMessageDialog(null,
            new HtmlPanel("help/about.html", 300, 200), "About",
            JOptionPane.INFORMATION_MESSAGE)));
    helpMenu.add(about);
    JMenuItem contents = new JMenuItem(SwingAction.of("Contents",
        e -> JOptionPane.showMessageDialog(null,
            new HtmlPanel("help/contents.html", 600, 350), "Contents",
            JOptionPane.INFORMATION_MESSAGE)));
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
      optionPane.addPropertyChangeListener(e -> {
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
      optionPane.addPropertyChangeListener(e -> {
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
}
