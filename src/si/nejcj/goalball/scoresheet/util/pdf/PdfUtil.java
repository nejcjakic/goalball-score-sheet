package si.nejcj.goalball.scoresheet.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import si.nejcj.goalball.scoresheet.exception.technical.InternalTechnicalException;

public class PdfUtil {

  static final Font TITLE_ROW_FONT = FontFactory.getFont(FontFactory.TIMES, 10,
      Font.BOLD, BaseColor.BLACK);
  static final Font DATA_ROW_FONT = FontFactory.getFont(FontFactory.TIMES, 10,
      Font.NORMAL, BaseColor.BLACK);

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
}
