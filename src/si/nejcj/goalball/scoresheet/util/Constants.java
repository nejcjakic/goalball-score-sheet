package si.nejcj.goalball.scoresheet.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {

  public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
      "dd.MM.yyyy");

  /**
   * Constants needed for reading/writing main properties file.
   */
  public static final String USER_HOME_PROPERTY_NAME = "user.home";
  public static final String CONFIG_FILE_NAME = "GoalballScoreSheetApp.properties";

  /**
   * Constants used for storing data in the properties file.
   */
  public static final String PROPERTY_LAST_VERSION = "last.version";
  public static final String PROPERTY_FIRST_RUN = "first.run";

  /**
   * Official levels, as stored in database.
   */
  public static final int OFFICIAL_LEVEL_ITO = 1;
  public static final int OFFICIAL_LEVEL_NATIONAL = 2;
  public static final int OFFICIAL_LEVEL_I = 3;
  public static final int OFFICIAL_LEVEL_II = 4;
  public static final int OFFICIAL_LEVEL_III = 5;

  /**
   * Constants for menu actions.
   */
  public enum MenuItems {
    // @formatter:off
    QUIT, 
    MANAGE_PLAYERS_DATA, 
    MANAGE_OFFICIALS_DATA, 
    UPDATE_DB_DATA, 
    ABOUT, 
    CONTENTS;
    // @formatter:on
  }

  /**
   * Constants for toolbar items.
   */
  public enum ToolbarItems {
    // @formatter:off
    START_TEST;
    // @formatter:on
  }

  /**
   * Constants for actions of ManageTeamDataPanel
   */
  public enum ManageTeamDataActions {
    // @formatter:off
    ADD_TEAM_ACTION,
    ADD_STAFF_ACTION,
    REMOVE_STAFF_ACTION,
    ADD_PLAYER_ACTION,
    REMOVE_PLAYER_ACTION;
    // @formatter:on
  }

  /**
   * Constants for listeners of TournamentPanel
   */
  public enum TournamentListeners {
    // @formatter:off
    TOURNAMENT_NAME,
    TOURNAMENT_LOCATION,
    TOURNAMENT_START_DATE, 
    TOURNAMENT_END_DATE,
    TEAM_ADD,
    TEAM_REMOVE,
    TEAM_EDIT,
    OFFICIAL_ADD,
    OFFICIAL_REMOVE,
    GAME_ADD,
    GAME_EDIT,
    GAME_REMOVE,
    GAME_RESULTS,
    GAME_IMPORT;
    // @formatter:on
  }

  /**
   * Constants for table models of TournamentPanel
   */
  public enum TournamentTableModels {
    // @formatter:off
    TEAMS_PARTICIPATING,
    TEAMS_ALL,
    OFFICIALS_TOURNAMENT,
    OFFICIALS_ALL,
    GAMES_ALL;
    // @formatter:on
  }

  /**
   * Constants for listeners of TournamentGameDataPanel
   * 
   */
  public enum TournamentGameListeners {
    // @formatter:off
    GAME_NUMBER,
    TEAM_A,
    TEAM_B,
    DATE,
    TIME,
    POOL,
    VENUE,
    GENDER,
    NEEDS_WINNER,
    REFEREE_1,
    REFEREE_2,
    TEN_SEC_1,
    TEN_SEC_2,
    SCORER,
    TIMER,
    BACKUP_TIMER,
    GOAL_JUDGE_1,
    GOAL_JUDGE_2,
    GOAL_JUDGE_3,
    GOAL_JUDGE_4;
    // @formatter:on
  }

  public enum TournamentGameTextType {
    // @formatter:off
    TIME,
    POOL,
    VENUE;
    // @formatter:on
  }

  /**
   * Constants for type of combobox listener of TournamentGamePanel
   * 
   */
  public enum TournamentGameComboBoxType {
    // @formatter:off
    TEAM_A,
    TEAM_B,
    DATE,
    REFEREE_1,
    REFEREE_2,
    TEN_SEC_1,
    TEN_SEC_2,
    SCORER,
    TIMER,
    BACKUP_TIMER,
    GOAL_JUDGE_1,
    GOAL_JUDGE_2,
    GOAL_JUDGE_3,
    GOAL_JUDGE_4;
    // @formatter:on
  }
}
