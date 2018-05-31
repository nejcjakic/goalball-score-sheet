package si.nejcj.goalball.scoresheet.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.db.entity.TournamentOfficial;
import si.nejcj.goalball.scoresheet.db.entity.TournamentTeam;
import si.nejcj.goalball.scoresheet.db.entity.util.RefereeGame;
import si.nejcj.goalball.scoresheet.db.entity.util.RefereeStats;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;

public class TournamentRefereesUtil extends PdfUtil {

  public static void createRefereesSchedule(File file,
      final List<TournamentGame> tournamentGames, String venue) {
    try {
      Collections.sort(tournamentGames);

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(
          new Rectangle(a4Size.getHeight(), a4Size.getWidth()), 5, 5, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      document.add(new Paragraph("Referees schedule " + venue));
      document.add(new Paragraph(" "));

      PdfPTable table = new PdfPTable(14);
      table.setWidthPercentage(100);
      
      table.setWidths(
          new int[] { 2, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 });

      table.addCell(new Phrase("Time", TITLE_ROW_FONT));
      table.addCell(new Phrase("Team 1", TITLE_ROW_FONT));
      table.addCell(new Phrase("Team 2", TITLE_ROW_FONT));
      table.addCell(new Phrase("Referee TS", TITLE_ROW_FONT));
      table.addCell(new Phrase("Referee FS", TITLE_ROW_FONT));
      table.addCell(new Phrase("Ten seconds", TITLE_ROW_FONT));
      table.addCell(new Phrase("Ten seconds", TITLE_ROW_FONT));
      table.addCell(new Phrase("Scorer", TITLE_ROW_FONT));
      table.addCell(new Phrase("Timer", TITLE_ROW_FONT));
      table.addCell(new Phrase("Backup timer", TITLE_ROW_FONT));
      table.addCell(new Phrase("Goal judge", TITLE_ROW_FONT));
      table.addCell(new Phrase("Goal judge", TITLE_ROW_FONT));
      table.addCell(new Phrase("Goal judge", TITLE_ROW_FONT));
      table.addCell(new Phrase("Goal judge", TITLE_ROW_FONT));
      for (TournamentGame tournamentGame : tournamentGames) {
        table.addCell(new Phrase(tournamentGame.getGameTime(), DATA_ROW_FONT));
        table.addCell(new Phrase(tournamentGame.getTeamA().getDisplayName(),
            DATA_ROW_FONT));
        table.addCell(new Phrase(tournamentGame.getTeamB().getDisplayName(),
            DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getReferee1()),
                DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getReferee2()),
                DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getTenSeconds1()),
                DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getTenSeconds2()),
                DATA_ROW_FONT));
        table.addCell(new Phrase(
            getNullSafeDisplayName(tournamentGame.getScorer()), DATA_ROW_FONT));
        table.addCell(new Phrase(
            getNullSafeDisplayName(tournamentGame.getTimer()), DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getBackupTimer()),
                DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge1()),
                DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge2()),
                DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge3()),
                DATA_ROW_FONT));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge4()),
                DATA_ROW_FONT));
      }
      document.add(table);

      document.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  public static void createRefereeGamesSheets(File file,
      final Map<TournamentOfficial, List<RefereeGame>> refereeGames) {
    try {
      Rectangle a4Size = PageSize.A4;
      Document document = new Document(
          new Rectangle(a4Size.getHeight(), a4Size.getWidth()), 15, 15, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      for (TournamentOfficial official : refereeGames.keySet()) {
        document.add(new Paragraph(official.getFullName()));
        document.add(new Paragraph(" "));

        List<RefereeGame> games = refereeGames.get(official);
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(50);

        table.addCell("Game no");
        table.addCell("Date");
        table.addCell("Time");
        table.addCell("Hall");
        table.addCell("Position");

        for (RefereeGame game : games) {
          table.addCell(game.getGameNo().toString());
          table.addCell(game.getGameDate().toString());
          table.addCell(game.getGameTime());
          table.addCell(game.getVenue());
          table.addCell(game.getPosition());
        }
        document.add(table);
        document.newPage();
      }
      document.close();
    } catch (IOException | DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  public static void createRefereeStats(File file,
      final List<RefereeStats> refereeStats) {
    try {
      Collections.sort(refereeStats);

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(
          new Rectangle(a4Size.getHeight(), a4Size.getWidth()), 15, 15, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      document.add(new Paragraph("Referee Stats"));
      document.add(new Paragraph(" "));

      PdfPTable table = new PdfPTable(5);
      table.setWidthPercentage(100);

      table.addCell(new Phrase("Official's name", TITLE_ROW_FONT));
      table.addCell(new Phrase("Games as referee", TITLE_ROW_FONT));
      table.addCell(new Phrase("Games as table official", TITLE_ROW_FONT));
      table.addCell(new Phrase("Games as goal judge", TITLE_ROW_FONT));
      table.addCell(new Phrase("Total games", TITLE_ROW_FONT));

      for (RefereeStats stats : refereeStats) {
        table.addCell(new Phrase(stats.getTournamentOfficial().getFullName(),
            DATA_ROW_FONT));
        table.addCell(new Phrase(stats.getGamesAsReferee(), DATA_ROW_FONT));
        table.addCell(
            new Phrase(stats.getGamesAsTableOfficial(), DATA_ROW_FONT));
        table.addCell(new Phrase(stats.getGamesAsGoalJudge(), DATA_ROW_FONT));
        table.addCell(new Phrase(stats.getTotalGames(), DATA_ROW_FONT));
      }
      document.add(table);

      document.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  public static void createRefStats(final List<TournamentGame> games) {
    Map<TournamentOfficial, List<TournamentGame>> refGames = new HashMap<>();
    Map<TournamentOfficial, Map<TournamentOfficial, Integer>> refPairs = new HashMap<>();
    Map<TournamentOfficial, Map<TournamentTeam, Integer>> refCountryGames = new HashMap<>();
    Map<TournamentOfficial, Map<TournamentTeam, Integer>> tenSecCountryGames = new HashMap<>();
    Map<TournamentOfficial, Integer> tenSecondsSameCountryGames = new HashMap<>();

    Map<TournamentOfficial, List<TournamentTeam>> teamGames = new HashMap<>();
    Map<TournamentOfficial, List<TournamentTeam>> totalGames = new HashMap<>();

    for (TournamentGame game : games) {
      String teamACountry = game.getTeamA().getCountry();
      String teamBCountry = game.getTeamB().getCountry();
      String ref1Country = game.getReferee1().getCountry();
      String ref2Country = game.getReferee2().getCountry();
      String tenSec1Country = game.getTenSeconds1().getCountry();
      String tenSec2Country = game.getTenSeconds2().getCountry();
      if (teamACountry.equalsIgnoreCase(ref1Country)
          || teamACountry.equalsIgnoreCase(ref2Country)
          || teamBCountry.equalsIgnoreCase(ref1Country)
          || teamBCountry.equalsIgnoreCase(ref2Country)) {
        System.err.println("REF AND TEAM COUNTRY MISMATCH");
        System.err.println(
            game.getGameNumber() + " " + game.getReferee1().getFullName()
                + " : " + game.getReferee2().getFullName());
        System.err.println(game.getTeamA().getCountry()+" "+game.getTeamB().getCountry());
      }
      if (teamACountry.equalsIgnoreCase(tenSec1Country)
          || teamACountry.equalsIgnoreCase(tenSec2Country)
          || teamBCountry.equalsIgnoreCase(tenSec1Country)
          || teamBCountry.equalsIgnoreCase(tenSec2Country)) {
        System.err.println("10 SECONDS REF AND TEAM COUNTRY MISMATCH");
        System.err.println(
            game.getGameNumber() + " " + game.getTenSeconds1().getFullName()
                + " : " + game.getTenSeconds2().getFullName());
        System.err.println(game.getTeamA().getCountry()+" "+game.getTeamB().getCountry());
      }
      if (ref1Country.equalsIgnoreCase(ref2Country)) {
        System.err.println("REF COUNTRY MISMATCH");
        System.err.println(
            game.getGameNumber() + " " + game.getReferee1().getFullName()
                + " : " + game.getReferee2().getFullName());
      }

      refGames.putIfAbsent(game.getReferee1(), new ArrayList<>());
      refGames.putIfAbsent(game.getReferee2(), new ArrayList<>());
      refGames.putIfAbsent(game.getTenSeconds1(), new ArrayList<>());
      refGames.putIfAbsent(game.getTenSeconds2(), new ArrayList<>());

      countTenSecondsGamesForSameCountry(tenSecondsSameCountryGames, game);
      countGamesPerCountryForEachRef(refCountryGames, tenSecCountryGames, game);
      countRefGamesAndTenSecGamesPerCountry(teamGames, totalGames, game);

      refGames.get(game.getReferee1()).add(game);
      refGames.get(game.getReferee2()).add(game);
      refGames.get(game.getTenSeconds1()).add(game);
      refGames.get(game.getTenSeconds2()).add(game);

    }
    printGamesPerCountryForEachRef(refCountryGames, tenSecCountryGames);
    // printAvailableGamesForEachRef(teamGames, totalGames);

    System.out.println("TEN SECOND SAME COUNTRY");

    for (Entry<TournamentOfficial, Integer> entry : tenSecondsSameCountryGames
        .entrySet()) {
      if (entry.getValue() > 0) {
        System.out.println(
            entry.getKey().getDisplayName() + " : " + entry.getValue());
      }
    }

    for (TournamentOfficial official : refGames.keySet()) {
      refPairs.putIfAbsent(official, new HashMap<>());
      for (TournamentOfficial official2 : refGames.keySet()) {
        refPairs.get(official).putIfAbsent(official2, 0);
      }
    }

    for (TournamentGame game : games) {
      refPairs.get(game.getReferee1()).put(game.getReferee2(),
          refPairs.get(game.getReferee1()).get(game.getReferee2()) + 1);
      refPairs.get(game.getReferee2()).put(game.getReferee1(),
          refPairs.get(game.getReferee2()).get(game.getReferee1()) + 1);
    }

    List<TournamentOfficial> sortedKeys = new ArrayList<>(refGames.keySet());
    Collections.sort(sortedKeys,
        (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));

    try {
      PdfPTable table = new PdfPTable(12);
      table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
      table.setWidths(
          new float[] { 3f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f });
      table.addCell("REFEREE NAME");
      table.addCell("REF TS");
      table.addCell("REF FS");
      table.addCell("REF total");
      table.addCell("10 s");
      table.addCell("TOTAL");
      table.addCell("first");
      table.addCell("last");
      table.addCell("Hall 1");
      table.addCell("Hall 2");
      table.addCell("Male");
      table.addCell("Female");
      for (TournamentOfficial key : sortedKeys) {
        printRefStats(table, key, refGames.get(key));
      }

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(
          new Rectangle(a4Size.getHeight(), a4Size.getWidth()), 0, 0, 5, 5);
      PdfWriter.getInstance(document,
          new FileOutputStream("/docs/temp/RefStats.pdf"));
      document.open();
      document.add(table);
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      PdfPTable refPairsTable = new PdfPTable(17);
      refPairsTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
      refPairsTable.setWidths(new float[] { 3f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f,
          1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f });
      refPairsTable.addCell("");
      for (TournamentOfficial official : sortedKeys) {
        refPairsTable.addCell(official.getFullName());
      }
      for (TournamentOfficial official : sortedKeys) {
        addRefPairsData(refPairsTable, official, refPairs.get(official));
      }

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(
          new Rectangle(a4Size.getHeight(), a4Size.getWidth()), 0, 0, 5, 5);
      PdfWriter.getInstance(document,
          new FileOutputStream("/docs/temp/RefPairs.pdf"));
      document.open();
      document.add(refPairsTable);
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void printAvailableGamesForEachRef(
      Map<TournamentOfficial, List<TournamentTeam>> teamGames,
      Map<TournamentOfficial, List<TournamentTeam>> totalGames) {
    List<String> REF_COUNTRIES = Arrays.asList("Russia", "Canada", "Australia",
        "Germany", "Thailand", "Japan", "Finland", "Great Britain", "Brazil",
        "Turkey", "Lithuania", "Ukraine");
    Map<String, List<String>> referees = new HashMap<>();
    referees.put("Russia", Arrays.asList("Alexey"));
    referees.put("Canada", Arrays.asList("Claude", "Janice"));
    referees.put("Australia", Arrays.asList("Warrick"));
    referees.put("Germany", Arrays.asList("Thomas", "Alex"));
    referees.put("Thailand", Arrays.asList("Woradat"));
    referees.put("Japan", Arrays.asList("Yoshi"));
    referees.put("Finland", Arrays.asList("Juha V", "Juha H"));
    referees.put("Great Britain", Arrays.asList("Robert"));
    referees.put("Brazil", Arrays.asList("Liana"));
    referees.put("Turkey", Arrays.asList("Bulent"));
    referees.put("Lithuania", Arrays.asList("Vilma V", "Vilma B"));
    referees.put("Ukraine", Arrays.asList("Svitlana"));

    Map<String, Map<String, Set<String>>> printGames = new HashMap<>();
    System.out.println("REFEREES AVAILABILITY");
    for (Entry<TournamentOfficial, List<TournamentTeam>> entry : teamGames
        .entrySet()) {
      // Set<String> teams = entry.getValue().stream()
      // .filter(t -> !refCountries.contains(t.getCountry()))
      // .map(t -> t.getCountry()).collect(Collectors.toSet());

      printGames.putIfAbsent(entry.getKey().getFullName(), new HashMap<>());
      Map<String, Set<String>> officialGames = printGames
          .get(entry.getKey().getFullName());
      officialGames.putIfAbsent("REF GAMES", new TreeSet<>());
      Set<String> games = officialGames.get("REF GAMES");

      // games.addAll(entry.getValue().stream()
      // .filter(t -> !REF_COUNTRIES.contains(t.getCountry()))
      // .map(t -> t.getCountry()).collect(Collectors.toSet()));

      ArrayList<String> refCountries = new ArrayList<>(REF_COUNTRIES);
      Set<String> removeFilter = new TreeSet<>();
      for (String country : refCountries) {
        for (TournamentTeam tournamentTeam : entry.getValue()) {
          if (country.equalsIgnoreCase(tournamentTeam.getCountry())) {
            removeFilter.add(country);
          }
        }
      }
      refCountries.removeAll(removeFilter);
      games.addAll(refCountries);

      // System.out.println(entry.getKey().getFullName());
      // System.out.println("REF GAMES");
      // teams.forEach(t -> System.out.print(t + ", "));
      // System.out.println();

    }

    for (Entry<TournamentOfficial, List<TournamentTeam>> entry : totalGames
        .entrySet()) {
      // Set<String> teams = entry.getValue().stream()
      // .filter(t -> !refCountries.contains(t.getCountry()))
      // .map(t -> t.getCountry()).collect(Collectors.toSet());
      // System.out.println(entry.getKey().getFullName());
      // System.out.println("TOTAL GAMES");
      // teams.forEach(t -> System.out.print(t + ", "));
      // System.out.println();
      printGames.putIfAbsent(entry.getKey().getFullName(), new HashMap<>());
      Map<String, Set<String>> officialGames = printGames
          .get(entry.getKey().getFullName());
      officialGames.putIfAbsent("" + "TOTAL GAMES", new TreeSet<>());
      Set<String> games = officialGames.get("TOTAL GAMES");
      // games.addAll(entry.getValue().stream()
      // .filter(t -> !refCountries.contains(t.getCountry()))
      // .map(t -> t.getCountry()).collect(Collectors.toSet()));
      ArrayList<String> refCountries = new ArrayList<>(REF_COUNTRIES);
      Set<String> removeFilter = new TreeSet<>();
      for (String country : refCountries) {
        for (TournamentTeam tournamentTeam : entry.getValue()) {
          if (country.equalsIgnoreCase(tournamentTeam.getCountry())) {
            removeFilter.add(country);
          }
        }
      }
      refCountries.removeAll(removeFilter);
      games.addAll(refCountries);
    }

    for (Entry<String, Map<String, Set<String>>> entry : printGames
        .entrySet()) {
      System.out.println(entry.getKey());
      Map<String, Set<String>> games = entry.getValue();
      for (Entry<String, Set<String>> entry2 : games.entrySet()) {
        System.out.println(entry2.getKey());
        entry2.getValue().forEach(t -> System.out.print(t + ", "));
        System.out.println();
        List<String> refs = new ArrayList<>();
        for (String country : entry2.getValue()) {
          refs.addAll(referees.get(country));
        }
        refs.forEach(r -> System.out.print(r + ", "));
        System.out.println();
      }
      System.out.println();
    }
  }

  private static void countRefGamesAndTenSecGamesPerCountry(
      Map<TournamentOfficial, List<TournamentTeam>> teamGames,
      Map<TournamentOfficial, List<TournamentTeam>> totalGames,
      TournamentGame game) {
    teamGames.putIfAbsent(game.getReferee1(), new ArrayList<>());
    teamGames.putIfAbsent(game.getReferee2(), new ArrayList<>());
    totalGames.putIfAbsent(game.getReferee1(), new ArrayList<>());
    totalGames.putIfAbsent(game.getReferee2(), new ArrayList<>());
    totalGames.putIfAbsent(game.getTenSeconds1(), new ArrayList<>());
    totalGames.putIfAbsent(game.getTenSeconds2(), new ArrayList<>());

    teamGames.get(game.getReferee1()).add(game.getTeamA());
    teamGames.get(game.getReferee1()).add(game.getTeamB());
    teamGames.get(game.getReferee2()).add(game.getTeamA());
    teamGames.get(game.getReferee2()).add(game.getTeamB());
    totalGames.get(game.getReferee1()).add(game.getTeamA());
    totalGames.get(game.getReferee1()).add(game.getTeamB());
    totalGames.get(game.getReferee2()).add(game.getTeamA());
    totalGames.get(game.getReferee2()).add(game.getTeamB());
    totalGames.get(game.getTenSeconds1()).add(game.getTeamA());
    totalGames.get(game.getTenSeconds1()).add(game.getTeamB());
    totalGames.get(game.getTenSeconds2()).add(game.getTeamA());
    totalGames.get(game.getTenSeconds2()).add(game.getTeamB());
  }

  private static void printGamesPerCountryForEachRef(
      Map<TournamentOfficial, Map<TournamentTeam, Integer>> refCountryGames,
      Map<TournamentOfficial, Map<TournamentTeam, Integer>> tenSecCountryGames) {
    System.out.println("Ref country games");
    for (Entry<TournamentOfficial, Map<TournamentTeam, Integer>> entry : refCountryGames
        .entrySet()) {
      TournamentOfficial official = entry.getKey();
      System.out.println(official.getFullName() + " : ");
      List<TournamentTeam> sortedKeys = new ArrayList<>(
          entry.getValue().keySet());
      Collections.sort(sortedKeys, new Comparator<TournamentTeam>() {

        @Override
        public int compare(TournamentTeam team1, TournamentTeam team2) {
          String genderSorter1 = team1.isMale() ? "a" : "b";
          String genderSorter2 = team2.isMale() ? "a" : "b";

          return genderSorter1.concat(team1.getSimpleName())
              .compareTo(genderSorter2.concat(team2.getSimpleName()));
        }
      });

      for (TournamentTeam key : sortedKeys) {
        System.out.println("\t" + key.getDisplayName() + " "
            + (key.isMale() ? "M" : "F") + " : " + entry.getValue().get(key));
      }
      System.out.println();
    }

    System.out.println("10 SEC country games");
    for (Entry<TournamentOfficial, Map<TournamentTeam, Integer>> entry : tenSecCountryGames
        .entrySet()) {
      TournamentOfficial official = entry.getKey();
      System.out.println(official.getFullName() + " : ");
      List<TournamentTeam> sortedKeys = new ArrayList<>(
          entry.getValue().keySet());
      Collections.sort(sortedKeys, new Comparator<TournamentTeam>() {

        @Override
        public int compare(TournamentTeam team1, TournamentTeam team2) {
          String genderSorter1 = team1.isMale() ? "a" : "b";
          String genderSorter2 = team2.isMale() ? "a" : "b";

          return genderSorter1.concat(team1.getSimpleName())
              .compareTo(genderSorter2.concat(team2.getSimpleName()));
        }
      });

      for (TournamentTeam key : sortedKeys) {
        System.out.println("\t" + key.getDisplayName() + " "
            + (key.isMale() ? "M" : "F") + " : " + entry.getValue().get(key));
      }
      System.out.println();
    }
  }

  private static void countGamesPerCountryForEachRef(
      Map<TournamentOfficial, Map<TournamentTeam, Integer>> refCountryGames,
      Map<TournamentOfficial, Map<TournamentTeam, Integer>> tenSecCountryGames,
      TournamentGame game) {
    refCountryGames.putIfAbsent(game.getReferee1(), new HashMap<>());
    refCountryGames.putIfAbsent(game.getReferee2(), new HashMap<>());
    tenSecCountryGames.putIfAbsent(game.getTenSeconds1(), new HashMap<>());
    tenSecCountryGames.putIfAbsent(game.getTenSeconds2(), new HashMap<>());

    Map<TournamentTeam, Integer> ref1Games = refCountryGames
        .get(game.getReferee1());
    ref1Games.merge(game.getTeamA(), 1, Integer::sum);
    ref1Games.merge(game.getTeamB(), 1, Integer::sum);

    Map<TournamentTeam, Integer> ref2Games = refCountryGames
        .get(game.getReferee2());
    ref2Games.merge(game.getTeamA(), 1, Integer::sum);
    ref2Games.merge(game.getTeamB(), 1, Integer::sum);

    Map<TournamentTeam, Integer> tneSec1Games = tenSecCountryGames
        .get(game.getTenSeconds1());
    tneSec1Games.merge(game.getTeamA(), 1, Integer::sum);
    tneSec1Games.merge(game.getTeamB(), 1, Integer::sum);

    Map<TournamentTeam, Integer> tneSec2Games = tenSecCountryGames
        .get(game.getTenSeconds2());
    tneSec2Games.merge(game.getTeamA(), 1, Integer::sum);
    tneSec2Games.merge(game.getTeamB(), 1, Integer::sum);
  }

  private static void countTenSecondsGamesForSameCountry(
      Map<TournamentOfficial, Integer> tenSecondsSameCountryGames,
      TournamentGame game) {
    tenSecondsSameCountryGames.putIfAbsent(game.getTenSeconds1(), 0);
    tenSecondsSameCountryGames.putIfAbsent(game.getTenSeconds2(), 0);
    if (game.getTenSeconds1().getCountry()
        .equalsIgnoreCase(game.getTeamA().getCountry())
        || game.getTenSeconds1().getCountry()
            .equalsIgnoreCase(game.getTeamB().getCountry())) {
      tenSecondsSameCountryGames.merge(game.getTenSeconds1(), 1, Integer::sum);
    }
    if (game.getTenSeconds2().getCountry()
        .equalsIgnoreCase(game.getTeamA().getCountry())
        || game.getTenSeconds2().getCountry()
            .equalsIgnoreCase(game.getTeamB().getCountry())) {
      tenSecondsSameCountryGames.merge(game.getTenSeconds2(), 1, Integer::sum);
    }
  }

  private static void printRefStats(PdfPTable table,
      TournamentOfficial official, List<TournamentGame> games) {
    List<Integer> firstGames = Arrays.asList(1, 2, 19, 20, 39, 40, 57, 58, 77,
        78);
    List<Integer> lastGames = Arrays.asList(17, 18, 37, 38, 55, 56, 75, 76);

    Long refTsGames = games.stream()
        .filter(g -> g.getReferee1().equals(official)).count();
    Long refFsGames = games.stream()
        .filter(g -> g.getReferee2().equals(official)).count();
    Long refTotalGames = refTsGames + refFsGames;
    Long tenSecGames = games.stream()
        .filter(g -> (g.getTenSeconds1().equals(official)
            || g.getTenSeconds2().equals(official)))
        .count();
    Long totalGames = refTotalGames + tenSecGames;

    Long noOfFirstGames = games.stream()
        .filter(g -> firstGames.contains(g.getGameNumber())).count();
    Long noOfLastGames = games.stream()
        .filter(g -> lastGames.contains(g.getGameNumber())).count();

    Long hall1Games = games.stream().filter(g -> g.getVenue().equals("Hall 1"))
        .count();
    Long hall2Games = games.stream().filter(g -> g.getVenue().equals("Hall 2"))
        .count();

    Long maleGames = games.stream()
        .filter(g -> ((g.getReferee1().equals(official)
            || g.getReferee2().equals(official))
            && g.getGender().equals("Male")))
        .count();
    Long femaleGames = games.stream()
        .filter(g -> ((g.getReferee1().equals(official)
            || g.getReferee2().equals(official))
            && g.getGender().equals("Female")))
        .count();

    table.addCell(official.getDisplayName());
    table.addCell(refTsGames.toString());
    table.addCell(refFsGames.toString());
    table.addCell(refTotalGames.toString());
    table.addCell(tenSecGames.toString());
    table.addCell(totalGames.toString());
    table.addCell(noOfFirstGames.toString());
    table.addCell(noOfLastGames.toString());
    table.addCell(hall1Games.toString());
    table.addCell(hall2Games.toString());
    table.addCell(maleGames.toString());
    table.addCell(femaleGames.toString());
  }

  private static void addRefPairsData(PdfPTable table,
      TournamentOfficial official, Map<TournamentOfficial, Integer> matrix) {
    List<TournamentOfficial> sortedKeys = new ArrayList<>(matrix.keySet());
    Collections.sort(sortedKeys,
        (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));

    table.addCell(official.getFullName());// TODO: Switch to display name
    for (TournamentOfficial pair : sortedKeys) {
      table.addCell(matrix.get(pair).toString());
    }
  }

  private static String getNullSafeDisplayName(TournamentOfficial official) {
    String initial = official == null ? ""
        : " " + official.getLastName().charAt(0) + ".";
    return official == null ? "" : official.getFirstName() + initial;
  }
}
