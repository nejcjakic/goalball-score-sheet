package si.nejcj.goalball.scoresheet.tournament.panel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class GameSelectionPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private JTextField fromGameTF;
  private JTextField toGameTF;

  public GameSelectionPanel() {

    JLabel fromGameLBL = new JLabel("From game");
    JLabel toGameLBL = new JLabel("To game");

    fromGameTF = new JTextField();
    fromGameTF.setColumns(10);

    toGameTF = new JTextField();
    toGameTF.setColumns(10);

    // !!!!!!!!!!!!!!!!!!!!!!!!!!
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout
        .createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup().addContainerGap()
            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(fromGameLBL)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(fromGameTF, GroupLayout.PREFERRED_SIZE, 86,
                        GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(toGameLBL).addGap(18).addComponent(toGameTF,
                        GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(15, Short.MAX_VALUE)));
    groupLayout
        .setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(fromGameLBL).addComponent(fromGameTF,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(toGameLBL).addComponent(toGameTF,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE)));
    setLayout(groupLayout);
    // !!!!!!!!!!!!!!!!!!!!!!!!!!
  }

  public Integer getToGame() {
    try {
      return Integer.parseInt(toGameTF.getText());
    } catch (Exception e) {
      return null;
    }
  }

  public Integer getFromGame() {
    try {
      return Integer.parseInt(fromGameTF.getText());
    } catch (Exception e) {
      return null;
    }
  }
}
