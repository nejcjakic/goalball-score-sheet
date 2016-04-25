package si.nejcj.goalball.scoresheet;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
  /**
   * Creates the main frame of the program using the system look and feel and
   * sets its title to 'Goalball ScoreSheet'.
   */
  public MainFrame() {
    super("Goalball ScoreSheet");
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width / 6, dim.height / 6);
    setPreferredSize(new Dimension((int) (dim.width / 1.5),
        (int) (dim.height / 1.5)));

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Problems with setting window visual parameters.", "Warning",
          JOptionPane.WARNING_MESSAGE);
    }
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
