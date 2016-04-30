package si.nejcj.goalball.scoresheet.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import si.nejcj.goalball.scoresheet.db.entity.Team;
import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.db.entity.TournamentPlayer;
import si.nejcj.goalball.scoresheet.db.entity.util.GameResult;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;

public class TournamentDataUtil extends PdfUtil {

  static final Font TEAM_DISPLAY_NAME_FONT = FontFactory
      .getFont(FontFactory.TIMES, 120, Font.BOLD, BaseColor.BLACK);

  public static void createTeamDisplayNames(File file, final List<Team> teams) {
    try {
      Document document = new Document(PageSize.A4.rotate());
      PdfWriter writer = PdfWriter.getInstance(document,
          new FileOutputStream(file));
      document.open();
      for (Team team : teams) {
        PdfPTable table = new PdfPTable(1);
        Phrase p = new Phrase(team.getTeamName().toUpperCase(),
            TEAM_DISPLAY_NAME_FONT);
        PdfPCell cell = new PdfPCell(p);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(1);
        table.addCell(cell);

        table.setTotalWidth(PageSize.A4.rotate().getWidth());
        table.setLockedWidth(true);
        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate template = canvas.createTemplate(table.getTotalWidth(),
            table.getTotalHeight());
        table.writeSelectedRows(0, -1, 0, table.getTotalHeight(), template);
        Image img = Image.getInstance(template);
        img.scaleToFit(PageSize.A4.rotate().getWidth(),
            PageSize.A4.rotate().getHeight());
        img.setAbsolutePosition(0,
            (PageSize.A4.rotate().getHeight() - table.getTotalHeight()) / 2);
        document.add(img);
        document.newPage();
      }

      document.close();
    } catch (IOException | DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }

  }

  public static void createTournamentResults(File file,
      final Map<String, List<GameResult>> endResults,
      final Map<String, List<TournamentGame>> endGames,
      final Map<TournamentPlayer, Integer> tournamentScorers) {
    try {
      Rectangle a4Size = PageSize.A4;
      Document document = new Document(
          new Rectangle(a4Size.getWidth(), a4Size.getHeight()), 5, 5, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      // document.add(new Paragraph(" "));
      // document.add(new Paragraph("Final tournament results"));
      // document.add(new Paragraph(" "));
      // document.add(createFinalResultsTable());

      Set<String> gamePools = endResults.keySet();

      String finalKey = getKeyForType(gamePools, PoolType.FINAL);
      if (finalKey != null) {
        List<TournamentGame> finalGames = endGames.get(finalKey);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Final game result"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(finalGames));

      }

      String place3key = getKeyForType(gamePools, PoolType.PLACE_3);
      if (place3key != null) {
        List<TournamentGame> place3Games = endGames.get(place3key);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("3rd place game result"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(place3Games));
      }

      String place5key = getKeyForType(gamePools, PoolType.PLACE_5);
      if (place5key != null) {
        List<TournamentGame> place5Games = endGames.get(place5key);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("5th place game result"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(place5Games));
      }

      String semiFinalKey = getKeyForType(gamePools, PoolType.SEMI_FINAL);
      if (semiFinalKey != null) {
        List<TournamentGame> semiFinalGames = endGames.get(semiFinalKey);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Semi final results"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(semiFinalGames));
      }

      String quarterFinalKey = getKeyForType(gamePools, PoolType.QUARTER_FINAL);
      if (quarterFinalKey != null) {
        List<TournamentGame> quarterFinalGames = endGames.get(quarterFinalKey);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Quarter final results"));
        document.add(new Paragraph(" "));
        document.add(createGamesResultsTable(quarterFinalGames));
      }

      String rankKey = getKeyForType(gamePools, PoolType.RANK);
      if (rankKey != null) {
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
      tournamentResultsTable
          .setWidths(new float[] { 1f, 5f, 2f, 2f, 2f, 2f, 2f, 2f, 2f });
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
        tournamentResultsTable
            .addCell(new Phrase(String.valueOf(i++), dataRowFont));
        tournamentResultsTable
            .addCell(new Phrase(result.getTeamName(), dataRowFont));
        tournamentResultsTable
            .addCell(new Phrase(String.valueOf(result.getWins()), dataRowFont));
        tournamentResultsTable.addCell(
            new Phrase(String.valueOf(result.getDraws()), dataRowFont));
        tournamentResultsTable.addCell(
            new Phrase(String.valueOf(result.getLosses()), dataRowFont));
        tournamentResultsTable.addCell(
            new Phrase(String.valueOf(result.getGoalsScored()), dataRowFont));
        tournamentResultsTable.addCell(
            new Phrase(String.valueOf(result.getGoalsConceded()), dataRowFont));
        tournamentResultsTable.addCell(new Phrase(
            String.valueOf(result.getGoalDifference()), dataRowFont));
        tournamentResultsTable.addCell(
            new Phrase(String.valueOf(result.getPoints()), titleRowFont));
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
      TournamentPlayer player = entry.getKey();
      topScorersTable.addCell(new Phrase(player.getFullName(), dataRowFont));
      topScorersTable.addCell(new Phrase(player.getTeamName(), dataRowFont));
      topScorersTable
          .addCell(new Phrase(entry.getValue().toString(), dataRowFont));
    }
    return topScorersTable;
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

  // TODO: Hack for MEGL
  // private static PdfPTable createFinalResultsTable() throws DocumentException
  // {
  // PdfPTable gameResultsTable = new PdfPTable(2);
  // gameResultsTable.setWidthPercentage(30);
  // gameResultsTable.setWidths(new float[] { 1f, 3f });
  // gameResultsTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
  // gameResultsTable.addCell(new Phrase("Place", titleRowFont));
  // gameResultsTable.addCell(new Phrase("Team", titleRowFont));
  //
  // gameResultsTable.addCell(new Phrase("1", dataRowFont));
  // gameResultsTable.addCell(new Phrase("Karantanija / Slovenia",
  // dataRowFont));
  // gameResultsTable.addCell(new Phrase("2", dataRowFont));
  // gameResultsTable.addCell(new Phrase("Hungary 2 / Hungary", dataRowFont));
  // gameResultsTable.addCell(new Phrase("3", dataRowFont));
  // gameResultsTable.addCell(new Phrase("Hungary 1 / Hungary", dataRowFont));
  // gameResultsTable.addCell(new Phrase("4", dataRowFont));
  // gameResultsTable
  // .addCell(new Phrase("Storm clouds / Slovenia", dataRowFont));
  // gameResultsTable.addCell(new Phrase("5", dataRowFont));
  // gameResultsTable.addCell(new Phrase("Croatia", dataRowFont));
  // gameResultsTable.addCell(new Phrase("6", dataRowFont));
  // gameResultsTable.addCell(new Phrase("Zagreb / Croatia", dataRowFont));
  // return gameResultsTable;
  // }
}