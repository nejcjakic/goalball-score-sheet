package si.nejcj.goalball.scoresheet.admin.panel;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ManageAppDataPanel extends JPanel {
  private JTabbedPane tabbedPane;

  public ManageAppDataPanel() {
    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    add(tabbedPane);
  }

  public void addTab(String title, Icon icon, JPanel component, String tip) {
    tabbedPane.addTab(title, icon, component, tip);
  }
}
