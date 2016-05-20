package si.nejcj.goalball.scoresheet.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.itextpdf.text.BadElementException;
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

  public static void createGamesSchedule(File file,
      final List<TournamentGame> tournamentGames, String venue) {
    try {
      Map<String, List<TournamentGame>> gamesByGroup = filterGamesByGroup(
          tournamentGames);

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(a4Size, 5, 5, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      document.add(new Paragraph("Games schedule " + venue));
      document.add(new Paragraph(" "));

      for (String group : gamesByGroup.keySet()) {
        document.add(new Paragraph(group));
        document.add(new Paragraph(" "));

        List<TournamentGame> groupGames = gamesByGroup.get(group);
        Collections.sort(groupGames);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(60);
        table.setWidths(new float[] { 1f, 2f, 3f, 3f });

        table.addCell(new Phrase("Time", TITLE_ROW_FONT));
        table.addCell(new Phrase("Group", TITLE_ROW_FONT));
        table.addCell(new Phrase("Team 1", TITLE_ROW_FONT));
        table.addCell(new Phrase("Team 2", TITLE_ROW_FONT));
        for (TournamentGame tournamentGame : groupGames) {
          table
              .addCell(new Phrase(tournamentGame.getGameTime(), DATA_ROW_FONT));
          table.addCell(new Phrase(tournamentGame.getPool(), DATA_ROW_FONT));
          table.addCell(new Phrase(tournamentGame.getTeamA().getDisplayName(),
              DATA_ROW_FONT));
          table.addCell(new Phrase(tournamentGame.getTeamB().getDisplayName(),
              DATA_ROW_FONT));
        }
        document.add(table);
      }

      document.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  public static void createTeamDisplayNames(File file, final List<Team> teams) {
    try {
      Rectangle pageSize = PageSize.A4.rotate();
      Document document = new Document(pageSize);
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

        Image img = fitTableToPage(pageSize, writer.getDirectContent(), table);
        document.add(img);
        document.newPage();
      }

      document.close();
    } catch (IOException | DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  public static void createResultInputTable(File file,
      List<TournamentGame> tournamentGames) {
    try {
      Map<String, Set<String>> groups = filterGroups(tournamentGames);

      Rectangle pageSize = PageSize.A4.rotate();
      Document document = new Document(pageSize);
      PdfWriter writer = PdfWriter.getInstance(document,
          new FileOutputStream(file));
      document.open();

      for (String group : groups.keySet()) {
        document.add(new Paragraph(group.toUpperCase()));
        document.add(new Paragraph(" "));

        Set<String> teams = groups.get(group);

        int numberOfTeams = teams.size();
        PdfPTable table = new PdfPTable(numberOfTeams + 2);

        PdfPCell darkCell = new PdfPCell();
        darkCell.setBackgroundColor(BaseColor.BLACK);
        PdfPCell emptyCell = new PdfPCell();

        table.addCell(darkCell);

        for (String team : teams) {
          Phrase p = new Phrase(team, TITLE_ROW_FONT);
          PdfPCell cell = new PdfPCell(p);
          cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
          cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
          table.addCell(cell);
        }
        table.addCell(new Phrase("POINTS", TITLE_ROW_FONT));

        for (int i = 0; i < numberOfTeams; i++) {
          String team = (String) teams.toArray()[i];
          Phrase p = new Phrase("\n" + team + "\n", TITLE_ROW_FONT);
          PdfPCell cell = new PdfPCell(p);
          cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
          cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
          table.addCell(cell);
          for (int j = 0; j < numberOfTeams + 1; j++) {
            if (i == j) {
              table.addCell(darkCell);
            } else {
              table.addCell(emptyCell);
            }
          }
        }

        Image img = fitTableToPage(pageSize, writer.getDirectContent(), table);
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

      List<String> groupNames = getKeysForType(gamePools, PoolType.GENERAL);
      if (!groupNames.isEmpty()) {
        Collections.sort(groupNames);
        for (String groupName : groupNames) {
          List<GameResult> groupResults = endResults.get(groupName);
          List<TournamentGame> groupGames = endGames.get(groupName);
          Collections.sort(groupResults);

          document.add(new Paragraph(" "));
          document.add(new Paragraph(groupName + " results"));
          document.add(new Paragraph(" "));

          PdfPTable tournamentResultsTable = new PdfPTable(9);
          tournamentResultsTable.setWidthPercentage(85);
          tournamentResultsTable
              .setWidths(new float[] { 1f, 5f, 2f, 2f, 2f, 2f, 2f, 2f, 2f });
          tournamentResultsTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);

          tournamentResultsTable.addCell(new Phrase("Pos", TITLE_ROW_FONT));
          tournamentResultsTable
              .addCell(new Phrase("Team name", TITLE_ROW_FONT));
          tournamentResultsTable.addCell(new Phrase("Wins", TITLE_ROW_FONT));
          tournamentResultsTable.addCell(new Phrase("Draws", TITLE_ROW_FONT));
          tournamentResultsTable.addCell(new Phrase("Loses", TITLE_ROW_FONT));
          tournamentResultsTable.addCell(new Phrase("GF", TITLE_ROW_FONT));
          tournamentResultsTable.addCell(new Phrase("GA", TITLE_ROW_FONT));
          tournamentResultsTable.addCell(new Phrase("Dif", TITLE_ROW_FONT));
          tournamentResultsTable.addCell(new Phrase("Points", TITLE_ROW_FONT));

          int i = 1;
          for (GameResult result : groupResults) {
            tournamentResultsTable
                .addCell(new Phrase(String.valueOf(i++), DATA_ROW_FONT));
            tournamentResultsTable
                .addCell(new Phrase(result.getTeamName(), DATA_ROW_FONT));
            tournamentResultsTable.addCell(
                new Phrase(String.valueOf(result.getWins()), DATA_ROW_FONT));
            tournamentResultsTable.addCell(
                new Phrase(String.valueOf(result.getDraws()), DATA_ROW_FONT));
            tournamentResultsTable.addCell(
                new Phrase(String.valueOf(result.getLosses()), DATA_ROW_FONT));
            tournamentResultsTable.addCell(new Phrase(
                String.valueOf(result.getGoalsScored()), DATA_ROW_FONT));
            tournamentResultsTable.addCell(new Phrase(
                String.valueOf(result.getGoalsConceded()), DATA_ROW_FONT));
            tournamentResultsTable.addCell(new Phrase(
                String.valueOf(result.getGoalDifference()), DATA_ROW_FONT));
            tournamentResultsTable.addCell(
                new Phrase(String.valueOf(result.getPoints()), TITLE_ROW_FONT));
          }
          document.add(tournamentResultsTable);

          document.add(new Paragraph(" "));
          document.add(new Paragraph("Pool game results"));
          document.add(new Paragraph(" "));

          document.add(createGamesResultsTable(groupGames));
        }
      }

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
    gameResultsTable.addCell(new Phrase("Teams", TITLE_ROW_FONT));
    gameResultsTable.addCell(new Phrase("Score", TITLE_ROW_FONT));

    for (TournamentGame game : tournamentGames) {
      StringBuilder teams = new StringBuilder();
      teams.append(game.getTeamA().getSimpleName());
      teams.append(" : ");
      teams.append(game.getTeamB().getSimpleName());
      gameResultsTable.addCell(new Phrase(teams.toString(), DATA_ROW_FONT));

      StringBuilder gameResult = new StringBuilder();
      gameResult.append(game.getScoreTeamA());
      gameResult.append(" : ");
      gameResult.append(game.getScoreTeamB());
      gameResultsTable
          .addCell(new Phrase(gameResult.toString(), DATA_ROW_FONT));
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
    topScorersTable.addCell(new Phrase("Player", TITLE_ROW_FONT));
    topScorersTable.addCell(new Phrase("Team", TITLE_ROW_FONT));
    topScorersTable.addCell(new Phrase("Goals", TITLE_ROW_FONT));

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
      topScorersTable.addCell(new Phrase(player.getFullName(), DATA_ROW_FONT));
      topScorersTable.addCell(new Phrase(player.getTeamName(), DATA_ROW_FONT));
      topScorersTable
          .addCell(new Phrase(entry.getValue().toString(), DATA_ROW_FONT));
    }
    return topScorersTable;
  }

  // TODO: Move somewhere else
  private enum PoolType {
    FINAL, SEMI_FINAL, PLACE_3, PLACE_5, QUARTER_FINAL, RANK, GENERAL;
  }

  private static List<String> getKeysForType(Set<String> keySet,
      PoolType type) {
    List<String> keys = new ArrayList<>();
    if (type == PoolType.GENERAL) {
      for (String key : keySet) {
        if (key.toUpperCase().startsWith(GameResult.GENERAL_GAME)) {
          keys.add(key);
        }
      }
    }
    return keys;
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
      // case GENERAL:
      // if (key.equals(GameResult.GENERAL_GAME)) {
      // return key;
      // }
      // break;
      }
    }
    return null;
  }

  private static Image fitTableToPage(Rectangle pageSize, PdfContentByte canvas,
      PdfPTable table) throws BadElementException {
    table.setTotalWidth(pageSize.getWidth());
    table.setLockedWidth(true);
    PdfTemplate template = canvas.createTemplate(table.getTotalWidth(),
        table.getTotalHeight());
    table.writeSelectedRows(0, -1, 0, table.getTotalHeight(), template);
    Image img = Image.getInstance(template);
    img.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
    img.setAbsolutePosition(0,
        (pageSize.getHeight() - table.getTotalHeight()) / 2);
    return img;
  }

  private static Map<String, List<TournamentGame>> filterGamesByGroup(
      List<TournamentGame> tournamentGames) {
    return tournamentGames.stream()
        .collect(Collectors.groupingBy(TournamentGame::getPool));
  }

  private static Map<String, Set<String>> filterGroups(
      List<TournamentGame> tournamentGames) {
    Map<String, List<TournamentGame>> gamesByGroup = filterGamesByGroup(
        tournamentGames);

    Map<String, Set<String>> tournamentGroups = new TreeMap<String, Set<String>>();

    for (String group : gamesByGroup.keySet()) {
      tournamentGroups.putIfAbsent(group, new HashSet<String>());
      List<TournamentGame> games = gamesByGroup.get(group);
      for (TournamentGame game : games) {
        tournamentGroups.get(group)
            .add(game.getTeamA().getTeamName().toUpperCase());
        tournamentGroups.get(group)
            .add(game.getTeamB().getTeamName().toUpperCase());
      }
    }

    return tournamentGroups;
  }

  // TODO: Hack for MEGL
  // private static PdfPTable createFinalResultsTable() throws
  // DocumentException
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
