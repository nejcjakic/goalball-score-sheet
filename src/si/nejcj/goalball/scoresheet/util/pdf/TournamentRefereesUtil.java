package si.nejcj.goalball.scoresheet.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import si.nejcj.goalball.scoresheet.db.entity.TournamentGame;
import si.nejcj.goalball.scoresheet.db.entity.TournamentOfficial;
import si.nejcj.goalball.scoresheet.db.entity.util.TournamentStats;
import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;

public class TournamentRefereesUtil {

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

      Font titleRowFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD,
          BaseColor.BLACK);
      Font dataRowFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL,
          BaseColor.BLACK);

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
        table.addCell(new Phrase(
            getNullSafeDisplayName(tournamentGame.getReferee1()), dataRowFont));
        table.addCell(new Phrase(
            getNullSafeDisplayName(tournamentGame.getReferee2()), dataRowFont));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getTenSeconds1()),
                dataRowFont));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getTenSeconds2()),
                dataRowFont));
        table.addCell(new Phrase(
            getNullSafeDisplayName(tournamentGame.getScorer()), dataRowFont));
        table.addCell(new Phrase(
            getNullSafeDisplayName(tournamentGame.getTimer()), dataRowFont));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getBackupTimer()),
                dataRowFont));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge1()),
                dataRowFont));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge2()),
                dataRowFont));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge3()),
                dataRowFont));
        table.addCell(
            new Phrase(getNullSafeDisplayName(tournamentGame.getGoalJudge4()),
                dataRowFont));
      }
      document.add(table);

      document.close();
    } catch (IOException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    } catch (DocumentException e) {
      throw new InternalTechnicalException("Problem creating pdf file", e);
    }
  }

  public static void createTournamentStats(File file,
      final List<TournamentStats> tournamentStats) {
    try {
      Collections.sort(tournamentStats);

      Rectangle a4Size = PageSize.A4;
      Document document = new Document(
          new Rectangle(a4Size.getHeight(), a4Size.getWidth()), 5, 5, 20, 20);
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      document.add(new Paragraph("Tournament Stats"));
      document.add(new Paragraph(" "));

      PdfPTable table = new PdfPTable(4);
      table.setWidthPercentage(100);

      Font titleRowFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD,
          BaseColor.BLACK);
      Font dataRowFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL,
          BaseColor.BLACK);

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

  private static String getNullSafeDisplayName(TournamentOfficial official) {
    String initial = official == null ? ""
        : " " + official.getLastName().charAt(0) + ".";
    return official == null ? "" : official.getFirstName() + initial;
  }
}
