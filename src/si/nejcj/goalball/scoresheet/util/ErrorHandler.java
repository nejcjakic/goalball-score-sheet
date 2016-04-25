package si.nejcj.goalball.scoresheet.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ErrorHandler {

  public static final int MAX_MSG_LEN = 700;

  public static int userErr(String message) {
    return userErr(message, null, null);
  }

  public static int userErr(String message, String details) {
    return userErr(message, details, null);
  }

  public static int userErr(String message, String details, Throwable stack) {
    return showError(message, details, stack, "User Error");
  }

  public static int sysErr(String message, String details) {
    return sysErr(message, details, new Throwable());

  }

  public static int sysErr(String message, String details, Throwable throwable) {
    return showError(message, details, throwable, "System Error");
  }

  public static int userWrn(String message) {
    JOptionPane.showMessageDialog(null, message, "Warning",
        JOptionPane.WARNING_MESSAGE);
    return 0;
  }

  private static int showError(String message, String details, Throwable stack,
      String title) {
    int retVal = -1;

    if (message.length() > MAX_MSG_LEN) {
      message = message.substring(0, MAX_MSG_LEN) + " ...";
    }
    if (details != null || stack != null) {
      retVal = showDetails(message, details, stack, title);
    } else {
      JOptionPane.showMessageDialog(null, message, title,
          JOptionPane.ERROR_MESSAGE);
    }

    return retVal;
  }

  private static int showDetails(String message, String details,
      Throwable stack, String title) {

    String options[] = { "OK", "Details >>>" };

    int retVal = JOptionPane.showOptionDialog(null, message, title,
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options,
        null);

    if (retVal == 1) {

      StringBuilder sb = new StringBuilder();
      sb.append("\n");
      sb.append(message);
      sb.append("\n\n");

      if (details != null) {
        sb.append(details);
        sb.append('\n');
        sb.append('\n');
      }

      sb.append("Java ver   : " + System.getProperty("java.vm.version") + '\n');
      sb.append("Java vendor: " + System.getProperty("java.vm.vendor") + '\n');
      sb.append("Java vm    : " + System.getProperty("java.vm.name") + "\n\n");

      if (stack != null) {
        StringWriter s = new StringWriter();

        stack.printStackTrace(new PrintWriter(s));

        sb.append(s.toString());
      }

      System.out.println(sb.toString());

      JTextArea text = new JTextArea(sb.toString(), 25, 120);
      text.setEditable(false);
      JScrollPane scroller = new JScrollPane(text);
      JOptionPane.showMessageDialog(null, scroller, title,
          JOptionPane.ERROR_MESSAGE);
    }
    return retVal;
  }
}
