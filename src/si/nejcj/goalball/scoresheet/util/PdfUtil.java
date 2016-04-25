package si.nejcj.goalball.scoresheet.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.db.entity.TournamentOfficial;
import si.nejcj.goalball.scoresheet.db.entity.TournamentPlayer;
import si.nejcj.goalball.scoresheet.db.entity.TournamentTeam;
import si.nejcj.goalball.scoresheet.db.entity.util.GameResult;
import si.nejcj.goalball.scoresheet.db.entity.util.TournamentStats;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {

  private static final Font titleRowFont = FontFactory.getFont(
      FontFactory.TIMES, 10, Font.BOLD, BaseColor.BLACK);
  private static final Font dataRowFont = FontFactory.getFont(
      FontFactory.TIMES, 10, Font.NORMAL, BaseColor.BLACK);

  /**
   * Creates a new pdf file based on the given template and provided fields
   * values
   * 
   * @param fields
   *          Value of fields to modify in the template
   * @param templateFile
   *          Template pdf file to use
   * @param file
   *          File to save data
   * @throws InternalTechnicalException
   */
  public static void createPdfFromTemplate(final Map<String, String> fields,
      InputStream templateFile, File file) {
    PdfReader reader;
    try {
      reader = new PdfReader(templateFile);

      FileOutputStream fos = new FileOutputStream(file);
      PdfStamper stamper = new PdfStamper(reader, fos);
      AcroFields form = stamper.getAcroFields();

      for (String fieldName : fields.keySet()) {
        form.setField(fieldName, fields.get(fieldName));
      }
      stamper.setFormFlattening(true);
      stamper.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  // TODO: Move somewhere else
  private enum PoolType {
    FINAL, SEMI_FINAL, PLACE_3, PLACE_5, QUARTER_FINAL, RANK, GENERAL;
  }

  private static String getKeyForType(Set<String> keySet, PoolType type) {
    for (String key : keySet) {
      switch (type) {
      case FINAL:
        if (key.toUpperCase().equals("FINAL")) {
          return key;
        }
        break;
      case SEMI_FINAL:
        if (key.toUpperCase().contains("SEMI")) {
          return key;
        }
        break;
      case PLACE_3:
        if (key.toUpperCase().contains("3RD")) {
          return key;
        }
        break;
      case PLACE_5:
        if (key.toUpperCase().contains("5TH")) {
          return key;
        }
        break;
      case QUARTER_FINAL:
        if (key.toUpperCase().contains("QUARTER")) {
          return key;
        }
        break;
      case RANK:
        if (key.toUpperCase().contains("RANK")) {
          return key;
        }
        break;
      case GENERAL:
        if (key.equals(GameResult.GENERAL_GAME)) {
          return key;
        }
        break;
      }
    }
    return null;
  }

  public static void createTournamentResults(File file,
      final Map<String, List<GameResult>> endResults,
      final Map<String, List<TournamentGame>> endGames,
      final Map<TournamentPlayer, Integer> tournamentScorers) {
    try {
      Rectangle a4Size = PageSize.A4;
      Document document = new Document(new Rectangle(a4Size.getWidth(),
          a4Size.getHeight()), 5, 5, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

//      document.add(new Paragraph(" "));
//      document.add(new Paragraph("Final tournament results"));
//      document.add(new Paragraph(" "));
//      document.add(createFinalResultsTable());

      Set<String> gamePools = endResults.keySet();

      String finalKey = getKeyForType(gamePools, PoolType.FINAL);
      if (finalKey != null) {
        List<GameResult> finalResults = endResults.get(finalKey);
        List<TournamentGame> finalGames = endGames.get(finalKey);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Final game result"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(finalGames));

      }

      String place3key = getKeyForType(gamePools, PoolType.PLACE_3);
      if (place3key != null) {
        List<GameResult> place3Results = endResults.get(place3key);
        List<TournamentGame> place3Games = endGames.get(place3key);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("3rd place game result"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(place3Games));
      }

      String place5key = getKeyForType(gamePools, PoolType.PLACE_5);
      if (place5key != null) {
        List<GameResult> place5Results = endResults.get(place5key);
        List<TournamentGame> place5Games = endGames.get(place5key);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("5th place game result"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(place5Games));
      }

      String semiFinalKey = getKeyForType(gamePools, PoolType.SEMI_FINAL);
      if (semiFinalKey != null) {
        List<GameResult> semiFinalResults = endResults.get(semiFinalKey);
        List<TournamentGame> semiFinalGames = endGames.get(semiFinalKey);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Semi final results"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(semiFinalGames));
      }

      String quarterFinalKey = getKeyForType(gamePools, PoolType.QUARTER_FINAL);
      if (quarterFinalKey != null) {
        List<GameResult> quarterFinalResults = endResults.get(quarterFinalKey);
        List<TournamentGame> quarterFinalGames = endGames.get(quarterFinalKey);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Quarter final results"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(quarterFinalGames));
      }

      String rankKey = getKeyForType(gamePools, PoolType.RANK);
      if (rankKey != null) {
        List<GameResult> rankResults = endResults.get(rankKey);
        List<TournamentGame> rankGames = endGames.get(rankKey);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Ranking game results"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(rankGames));
      }

      List<GameResult> gameResults = endResults.get(GameResult.GENERAL_GAME);
      List<TournamentGame> tournamentGames = endGames
          .get(GameResult.GENERAL_GAME);
      Collections.sort(gameResults);

      document.add(new Paragraph(" "));
      document.add(new Paragraph("Pool results"));
      document.add(new Paragraph(" "));

      PdfPTable tournamentResultsTable = new PdfPTable(9);
      tournamentResultsTable.setWidthPercentage(85);
      tournamentResultsTable.setWidths(new float[] { 1f, 5f, 2f, 2f, 2f, 2f,
          2f, 2f, 2f });
      tournamentResultsTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);

      tournamentResultsTable.addCell(new Phrase("Pos", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("Team name", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("Wins", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("Draws", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("Loses", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("GF", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("GA", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("Dif", titleRowFont));
      tournamentResultsTable.addCell(new Phrase("Points", titleRowFont));

      int i = 1;
      for (GameResult result : gameResults) {
        tournamentResultsTable.addCell(new Phrase(String.valueOf(i++),
            dataRowFont));
        tournamentResultsTable.addCell(new Phrase(result.getTeamName(),
            dataRowFont));
        tournamentResultsTable.addCell(new Phrase(String.valueOf(result
            .getWins()), dataRowFont));
        tournamentResultsTable.addCell(new Phrase(String.valueOf(result
            .getDraws()), dataRowFont));
        tournamentResultsTable.addCell(new Phrase(String.valueOf(result
            .getLosses()), dataRowFont));
        tournamentResultsTable.addCell(new Phrase(String.valueOf(result
            .getGoalsScored()), dataRowFont));
        tournamentResultsTable.addCell(new Phrase(String.valueOf(result
            .getGoalsConceded()), dataRowFont));
        tournamentResultsTable.addCell(new Phrase(String.valueOf(result
            .getGoalDifference()), dataRowFont));
        tournamentResultsTable.addCell(new Phrase(String.valueOf(result
            .getPoints()), titleRowFont));
      }
      document.add(tournamentResultsTable);

      document.add(new Paragraph(" "));
      document.add(new Paragraph("Pool game results"));
      document.add(new Paragraph(" "));

      document.add(createGamesResultsTable(tournamentGames));

      document.add(new Paragraph(" "));
      document.add(new Paragraph("Top scorers"));
      document.add(new Paragraph(" "));

      PdfPTable topScorersTable = createTopScorersTable(tournamentScorers);
      document.add(topScorersTable);

      document.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  private static PdfPTable createTopScorersTable(
      final Map<TournamentPlayer, Integer> tournamentScorers)
      throws DocumentException {
    PdfPTable topScorersTable = new PdfPTable(3);
    topScorersTable.setWidthPercentage(30);
    topScorersTable.setWidths(new float[] { 5f, 4f, 2f });
    topScorersTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
    topScorersTable.addCell(new Phrase("Player", titleRowFont));
    topScorersTable.addCell(new Phrase("Team", titleRowFont));
    topScorersTable.addCell(new Phrase("Goals", titleRowFont));

    Set<Entry<TournamentPlayer, Integer>> entries = tournamentScorers
        .entrySet();
    List<Entry<TournamentPlayer, Integer>> scorersList = new ArrayList<Map.Entry<TournamentPlayer, Integer>>(
        entries);
    Collections.sort(scorersList,
        new Comparator<Map.Entry<TournamentPlayer, Integer>>() {
          @Override
          public int compare(Entry<TournamentPlayer, Integer> o1,
              Entry<TournamentPlayer, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
          }
        });
    for (Map.Entry<TournamentPlayer, Integer> entry : scorersList) {
      StringBuilder playerName = new StringBuilder();
      playerName.append(entry.getKey().getLastName());
      playerName.append(" ");
      playerName.append(entry.getKey().getFirstName());
      topScorersTable.addCell(new Phrase(playerName.toString(), dataRowFont));
      // TODO: Extremely ugly workaround for MEGL
      // TODO: To fix this make sure team name is populated when creating national teams
      String teamName = StringUtils.isEmpty(entry.getKey().getTeamName()) ? "Croatia"
          : entry.getKey().getTeamName();
      topScorersTable.addCell(new Phrase(teamName, dataRowFont));
      topScorersTable.addCell(new Phrase(entry.getValue().toString(),
          dataRowFont));
    }
    return topScorersTable;
  }

  // TODO: Hack for MEGL
//  private static PdfPTable createFinalResultsTable() throws DocumentException {
//    PdfPTable gameResultsTable = new PdfPTable(2);
//    gameResultsTable.setWidthPercentage(30);
//    gameResultsTable.setWidths(new float[] { 1f, 3f });
//    gameResultsTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
//    gameResultsTable.addCell(new Phrase("Place", titleRowFont));
//    gameResultsTable.addCell(new Phrase("Team", titleRowFont));
//
//    gameResultsTable.addCell(new Phrase("1", dataRowFont));
//    gameResultsTable.addCell(new Phrase("Karantanija / Slovenia", dataRowFont));
//    gameResultsTable.addCell(new Phrase("2", dataRowFont));
//    gameResultsTable.addCell(new Phrase("Hungary 2 / Hungary", dataRowFont));
//    gameResultsTable.addCell(new Phrase("3", dataRowFont));
//    gameResultsTable.addCell(new Phrase("Hungary 1 / Hungary", dataRowFont));
//    gameResultsTable.addCell(new Phrase("4", dataRowFont));
//    gameResultsTable
//        .addCell(new Phrase("Storm clouds / Slovenia", dataRowFont));
//    gameResultsTable.addCell(new Phrase("5", dataRowFont));
//    gameResultsTable.addCell(new Phrase("Croatia", dataRowFont));
//    gameResultsTable.addCell(new Phrase("6", dataRowFont));
//    gameResultsTable.addCell(new Phrase("Zagreb / Croatia", dataRowFont));
//    return gameResultsTable;
//  }

  private static PdfPTable createGamesResultsTable(
      List<TournamentGame> tournamentGames) throws DocumentException {
    PdfPTable gameResultsTable = new PdfPTable(2);
    gameResultsTable.setWidthPercentage(30);
    gameResultsTable.setWidths(new float[] { 3f, 1f });
    gameResultsTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
    gameResultsTable.addCell(new Phrase("Teams", titleRowFont));
    gameResultsTable.addCell(new Phrase("Score", titleRowFont));

    for (TournamentGame game : tournamentGames) {
      StringBuilder teams = new StringBuilder();
      teams.append(game.getTeamA().getSimpleName());
      teams.append(" : ");
      teams.append(game.getTeamB().getSimpleName());
      gameResultsTable.addCell(new Phrase(teams.toString(), dataRowFont));

      StringBuilder gameResult = new StringBuilder();
      gameResult.append(game.getScoreTeamA());
      gameResult.append(" : ");
      gameResult.append(game.getScoreTeamB());
      gameResultsTable.addCell(new Phrase(gameResult.toString(), dataRowFont));
    }
    return gameResultsTable;
  }

  public static void createTournamentStats(File file,
      final List<TournamentStats> tournamentStats) {
    try {
      Collections.sort(tournamentStats);

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(new Rectangle(a4Size.getHeight(),
          a4Size.getWidth()), 5, 5, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      document.add(new Paragraph("Tournament Stats"));
      document.add(new Paragraph(" "));

      PdfPTable table = new PdfPTable(4);
      table.setWidthPercentage(100);

      Font titleRowFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD,
          BaseColor.BLACK);
      Font dataRowFont = FontFactory.getFont(FontFactory.TIMES, 10,
          Font.NORMAL, BaseColor.BLACK);

      table.addCell(new Phrase("Official's name", titleRowFont));
      table.addCell(new Phrase("Games as referee", titleRowFont));
      table.addCell(new Phrase("Games as table official", titleRowFont));
      table.addCell(new Phrase("Games as goal judge", titleRowFont));

      for (TournamentStats stats : tournamentStats) {
        table.addCell(new Phrase(stats.getTournamentOfficial().getFullName(),
            dataRowFont));
        table.addCell(new Phrase(stats.getGamesAsReferee(), dataRowFont));
        table.addCell(new Phrase(stats.getGamesAsTableOfficial(), dataRowFont));
        table.addCell(new Phrase(stats.getGamesAsGoalJudge(), dataRowFont));
      }
      document.add(table);

      document.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  public static void createRefereesSchedule(File file,
      final List<TournamentGame> tournamentGames, String venue) {
    try {
      Collections.sort(tournamentGames);

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(new Rectangle(a4Size.getHeight(),
          a4Size.getWidth()), 5, 5, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      document.add(new Paragraph("Referees schedule " + venue));
      document.add(new Paragraph(" "));

      PdfPTable table = new PdfPTable(14);
      table.setWidthPercentage(100);

      Font titleRowFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD,
          BaseColor.BLACK);
      Font dataRowFont = FontFactory.getFont(FontFactory.TIMES, 10,
          Font.NORMAL, BaseColor.BLACK);

      table.addCell(new Phrase("Time", titleRowFont));
      table.addCell(new Phrase("Team 1", titleRowFont));
      table.addCell(new Phrase("Team 2", titleRowFont));
      table.addCell(new Phrase("Referee TS", titleRowFont));
      table.addCell(new Phrase("Referee FS", titleRowFont));
      table.addCell(new Phrase("Ten seconds", titleRowFont));
      table.addCell(new Phrase("Ten seconds", titleRowFont));
      table.addCell(new Phrase("Scorer", titleRowFont));
      table.addCell(new Phrase("Timer", titleRowFont));
      table.addCell(new Phrase("Backup timer", titleRowFont));
      table.addCell(new Phrase("Goal judge", titleRowFont));
      table.addCell(new Phrase("Goal judge", titleRowFont));
      table.addCell(new Phrase("Goal judge", titleRowFont));
      table.addCell(new Phrase("Goal judge", titleRowFont));
      for (TournamentGame tournamentGame : tournamentGames) {
        table.addCell(new Phrase(tournamentGame.getGameTime(), dataRowFont));
        table.addCell(new Phrase(tournamentGame.getTeamA().getDisplayName(),
            dataRowFont));
        table.addCell(new Phrase(tournamentGame.getTeamB().getDisplayName(),
            dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getReferee1()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getReferee2()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getTenSeconds1()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getTenSeconds2()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getScorer()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getTimer()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getBackupTimer()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getGoalJudge1()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getGoalJudge2()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getGoalJudge3()), dataRowFont));
        table.addCell(new Phrase(getNullSafeDisplayName(tournamentGame
            .getGoalJudge4()), dataRowFont));
      }
      document.add(table);

      document.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  private static String getNullSafeDisplayName(TournamentOfficial official) {
    String initial = official == null ? "" : " "
        + official.getLastName().charAt(0) + ".";
    return official == null ? "" : official.getFirstName() + initial;
  }
}
