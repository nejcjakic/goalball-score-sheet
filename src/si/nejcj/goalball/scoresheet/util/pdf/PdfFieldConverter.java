package si.nejcj.goalball.scoresheet.util.pdf;

import static si.nejcj.goalball.scoresheet.util.pdf.PdfFieldConstants.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.db.entity.TournamentPlayer;
import si.nejcj.goalball.scoresheet.db.entity.TournamentStaff;
import si.nejcj.goalball.scoresheet.db.entity.util.TournamentPlayerComparator;
import si.nejcj.goalball.scoresheet.util.Constants;

public class PdfFieldConverter {
  public static Map<String, String> createGameInformationMap(
      TournamentGame tournamentGame, List<TournamentPlayer> teamAPlayers,
      List<TournamentStaff> teamAStaff, List<TournamentPlayer> teamBPlayers,
      List<TournamentStaff> teamBStaff) {
    Map<String, String> gameInformationMap = new HashMap<String, String>();
    String gameNumber = tournamentGame.getGameNumber().toString();
    gameInformationMap.put(FIELD_GAME_NUMBER, gameNumber);
    String gameDate = Constants.DEFAULT_DATE_FORMAT
        .format(tournamentGame.getGameDate());
    gameInformationMap.put(FIELD_DATE_OF_GAME, gameDate);
    String gameTime = tournamentGame.getGameTime();
    gameInformationMap.put(FIELD_TIME_OF_GAME, gameTime);
    String gamePool = tournamentGame.getPool();
    gameInformationMap.put(FIELD_DIVISION_POOL, gamePool);
    String gameGender = tournamentGame.getGender();
    gameInformationMap.put(FIELD_GENDER, gameGender);
    String gameVenue = tournamentGame.getVenue();
    gameInformationMap.put(FIELD_VENUE, gameVenue);

    String referee1 = tournamentGame.getReferee1().getFullName();
    gameInformationMap.put(FIELD_REFEREE_TABLE_SIDE, referee1);
    String referee2 = tournamentGame.getReferee2().getFullName();
    gameInformationMap.put(FIELD_REFEREE_FAR_SIDE, referee2);
    String tenSec1 = tournamentGame.getTenSeconds1() == null ? null
        : tournamentGame.getTenSeconds1().getFullName();
    gameInformationMap.put(FIELD_TEN_SEC_TIMER_1, tenSec1);
    String tenSec2 = tournamentGame.getTenSeconds2() == null ? null
        : tournamentGame.getTenSeconds2().getFullName();
    gameInformationMap.put(FIELD_TEN_SEC_TIMER_2, tenSec2);

    String scorer = tournamentGame.getScorer() == null ? null
        : tournamentGame.getScorer().getFullName();
    gameInformationMap.put(FIELD_SCORER, scorer);
    String timer = tournamentGame.getTimer() == null ? null
        : tournamentGame.getTimer().getFullName();
    gameInformationMap.put(FIELD_TIMER, timer);
    String backupTimer = tournamentGame.getBackupTimer() == null ? null
        : tournamentGame.getBackupTimer().getFullName();
    gameInformationMap.put(FIELD_BACK_UP_TIMER, backupTimer);

    String goalJudge1 = tournamentGame.getGoalJudge1() == null ? null
        : tournamentGame.getGoalJudge1().getFullName();
    gameInformationMap.put(FIELD_GOAL_JUDGE_1, goalJudge1);
    String goalJudge2 = tournamentGame.getGoalJudge2() == null ? null
        : tournamentGame.getGoalJudge2().getFullName();
    gameInformationMap.put(FIELD_GOAL_JUDGE_2, goalJudge2);
    String goalJudge3 = tournamentGame.getGoalJudge3() == null ? null
        : tournamentGame.getGoalJudge3().getFullName();
    gameInformationMap.put(FIELD_GOAL_JUDGE_3, goalJudge3);
    String goalJudge4 = tournamentGame.getGoalJudge4() == null ? null
        : tournamentGame.getGoalJudge4().getFullName();
    gameInformationMap.put(FIELD_GOAL_JUDGE_4, goalJudge4);

    String teamAName = tournamentGame.getTeamA().getDisplayName();
    gameInformationMap.put(FIELD_TEAM_A_NAME, teamAName);
    gameInformationMap.put(FIELD_TEAM_A_SHORT_NAME,
        tournamentGame.getTeamA().getSimpleName());
    String teamBName = tournamentGame.getTeamB().getDisplayName();
    gameInformationMap.put(FIELD_TEAM_B_NAME, teamBName);
    gameInformationMap.put(FIELD_TEAM_B_SHORT_NAME,
        tournamentGame.getTeamB().getSimpleName());

    int loopCount = 1;
    Collections.sort(teamAPlayers, new TournamentPlayerComparator());
    for (TournamentPlayer player : teamAPlayers) {
      if (loopCount == 1) {
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_1_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_1_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_1_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 2) {
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_2_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_2_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_2_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 3) {
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_3_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_3_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_3_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 4) {
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_4_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_4_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_4_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 5) {
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_5_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_5_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_5_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 6) {
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_6_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_6_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_PLAYER_6_FIRST_NAME,
            player.getFirstName());
      }
      loopCount++;
    }
    loopCount = 1;

    for (TournamentStaff staff : teamAStaff) {
      if (loopCount == 1) {
        gameInformationMap.put(FIELD_TEAM_A_STAFF_1_LAST_NAME,
            staff.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_STAFF_1_FIRST_NAME,
            staff.getFirstName());
      } else if (loopCount == 2) {
        gameInformationMap.put(FIELD_TEAM_A_STAFF_2_LAST_NAME,
            staff.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_STAFF_2_FIRST_NAME,
            staff.getFirstName());
      } else if (loopCount == 3) {
        gameInformationMap.put(FIELD_TEAM_A_STAFF_3_LAST_NAME,
            staff.getLastName());
        gameInformationMap.put(FIELD_TEAM_A_STAFF_3_FIRST_NAME,
            staff.getFirstName());
      }
      loopCount++;
    }

    loopCount = 1;

    Collections.sort(teamBPlayers, new TournamentPlayerComparator());
    for (TournamentPlayer player : teamBPlayers) {
      if (loopCount == 1) {
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_1_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_1_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_1_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 2) {
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_2_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_2_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_2_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 3) {
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_3_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_3_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_3_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 4) {
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_4_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_4_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_4_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 5) {
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_5_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_5_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_5_FIRST_NAME,
            player.getFirstName());
      } else if (loopCount == 6) {
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_6_NUMBER,
            player.getPlayerNumber().toString());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_6_LAST_NAME,
            player.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_PLAYER_6_FIRST_NAME,
            player.getFirstName());
      }
      loopCount++;
    }
    loopCount = 1;

    for (TournamentStaff staff : teamBStaff) {
      if (loopCount == 1) {
        gameInformationMap.put(FIELD_TEAM_B_STAFF_1_LAST_NAME,
            staff.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_STAFF_1_FIRST_NAME,
            staff.getFirstName());
      } else if (loopCount == 2) {
        gameInformationMap.put(FIELD_TEAM_B_STAFF_2_LAST_NAME,
            staff.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_STAFF_2_FIRST_NAME,
            staff.getFirstName());
      } else if (loopCount == 3) {
        gameInformationMap.put(FIELD_TEAM_B_STAFF_3_LAST_NAME,
            staff.getLastName());
        gameInformationMap.put(FIELD_TEAM_B_STAFF_3_FIRST_NAME,
            staff.getFirstName());
      }
      loopCount++;
    }

    return gameInformationMap;
  }

  public static Map<String, String> createLineUpSheetMap(String teamName,
      List<TournamentPlayer> teamPlayers, List<TournamentStaff> teamStaff) {
    Map<String, String> dataMap = new HashMap<String, String>();

    dataMap.put(FIELD_TEAM_A_NAME, teamName);
    // gameInformationMap.put(FIELD_TEAM_A_SHORT_NAME,
    // tournamentGame.getTeamA().getSimpleName());

    int loopCount = 1;
    Collections.sort(teamPlayers, new TournamentPlayerComparator());
    for (TournamentPlayer player : teamPlayers) {
      if (loopCount == 1) {
        dataMap.put(FIELD_TEAM_A_PLAYER_1_NUMBER,
            player.getPlayerNumber().toString());
        dataMap.put(FIELD_TEAM_A_PLAYER_1_LAST_NAME, player.getLastName());
        dataMap.put(FIELD_TEAM_A_PLAYER_1_FIRST_NAME, player.getFirstName());
      } else if (loopCount == 2) {
        dataMap.put(FIELD_TEAM_A_PLAYER_2_NUMBER,
            player.getPlayerNumber().toString());
        dataMap.put(FIELD_TEAM_A_PLAYER_2_LAST_NAME, player.getLastName());
        dataMap.put(FIELD_TEAM_A_PLAYER_2_FIRST_NAME, player.getFirstName());
      } else if (loopCount == 3) {
        dataMap.put(FIELD_TEAM_A_PLAYER_3_NUMBER,
            player.getPlayerNumber().toString());
        dataMap.put(FIELD_TEAM_A_PLAYER_3_LAST_NAME, player.getLastName());
        dataMap.put(FIELD_TEAM_A_PLAYER_3_FIRST_NAME, player.getFirstName());
      } else if (loopCount == 4) {
        dataMap.put(FIELD_TEAM_A_PLAYER_4_NUMBER,
            player.getPlayerNumber().toString());
        dataMap.put(FIELD_TEAM_A_PLAYER_4_LAST_NAME, player.getLastName());
        dataMap.put(FIELD_TEAM_A_PLAYER_4_FIRST_NAME, player.getFirstName());
      } else if (loopCount == 5) {
        dataMap.put(FIELD_TEAM_A_PLAYER_5_NUMBER,
            player.getPlayerNumber().toString());
        dataMap.put(FIELD_TEAM_A_PLAYER_5_LAST_NAME, player.getLastName());
        dataMap.put(FIELD_TEAM_A_PLAYER_5_FIRST_NAME, player.getFirstName());
      } else if (loopCount == 6) {
        dataMap.put(FIELD_TEAM_A_PLAYER_6_NUMBER,
            player.getPlayerNumber().toString());
        dataMap.put(FIELD_TEAM_A_PLAYER_6_LAST_NAME, player.getLastName());
        dataMap.put(FIELD_TEAM_A_PLAYER_6_FIRST_NAME, player.getFirstName());
      }
      loopCount++;
    }
    loopCount = 1;

    for (TournamentStaff staff : teamStaff) {
      if (loopCount == 1) {
        dataMap.put(FIELD_TEAM_A_STAFF_1_LAST_NAME, staff.getLastName());
        dataMap.put(FIELD_TEAM_A_STAFF_1_FIRST_NAME, staff.getFirstName());
      } else if (loopCount == 2) {
        dataMap.put(FIELD_TEAM_A_STAFF_2_LAST_NAME, staff.getLastName());
        dataMap.put(FIELD_TEAM_A_STAFF_2_FIRST_NAME, staff.getFirstName());
      } else if (loopCount == 3) {
        dataMap.put(FIELD_TEAM_A_STAFF_3_LAST_NAME, staff.getLastName());
        dataMap.put(FIELD_TEAM_A_STAFF_3_FIRST_NAME, staff.getFirstName());
      }
      loopCount++;
    }

    return dataMap;
  }

}
