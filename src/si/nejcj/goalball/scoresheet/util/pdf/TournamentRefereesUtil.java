package si.nejcj.goalball.scoresheet.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(50);

        table.addCell("Game no");
        table.addCell("Time");
        table.addCell("Position");

        for (RefereeGame game : games) {
          table.addCell(game.getGameNo().toString());
          table.addCell(game.getGameTime());
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

  private static String getNullSafeDisplayName(TournamentOfficial official) {
    String initial = official == null ? ""
        : " " + official.getLastName().charAt(0) + ".";
    return official == null ? "" : official.getFirstName() + initial;
  }
}
