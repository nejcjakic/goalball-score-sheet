package si.nejcj.goalball.scoresheet.tournament.panel;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class GameResultsPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private JTextField scorersTeamATF;
  private JTextField scorersTeamBTF;
  private JLabel errorMessageLBL;

  /**
   * Create the panel.
   */
  public GameResultsPanel(String teamAName, String teamBName) {

    JLabel teamALBL = new JLabel(teamAName);
    scorersTeamATF = new JTextField();
    scorersTeamATF.setColumns(10);

    JLabel teamBLBL = new JLabel(teamBName);
    scorersTeamBTF = new JTextField();
    scorersTeamBTF.setColumns(10);

    errorMessageLBL = new JLabel();
    errorMessageLBL.setForeground(Color.RED);

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addGroup(
                        groupLayout
                            .createParallelGroup(Alignment.LEADING, false)
                            .addComponent(scorersTeamATF,
                                GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addComponent(teamALBL).addComponent(teamBLBL)
                            .addComponent(scorersTeamBTF))
                    .addComponent(errorMessageLBL))
            .addContainerGap(185, Short.MAX_VALUE)));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addComponent(teamALBL)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(scorersTeamATF, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(18)
            .addComponent(teamBLBL)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(scorersTeamBTF, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.UNRELATED)
            .addComponent(errorMessageLBL)
            .addContainerGap(166, Short.MAX_VALUE)));
    setLayout(groupLayout);
  }

  public String getScorersTeamA() {
    return scorersTeamATF.getText();
  }

  public String getScorersTeamB() {
    return scorersTeamBTF.getText();
  }

  public void setErrorText(String text) {
    errorMessageLBL.setText(text);
  }
}
