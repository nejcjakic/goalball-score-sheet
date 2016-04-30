package si.nejcj.goalball.scoresheet.admin.panel;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.lang3.StringUtils;

import si.nejcj.goalball.scoresheet.db.entity.Team;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class AddTeamPanel extends JPanel {

  private JTextField teamNameTF;
  private JCheckBox nationalTeamCB;
  private JComboBox countryCB;

  public AddTeamPanel(ComboBoxModel countries) {
    super();
    Dimension sizeDim = new Dimension(250, 110);
    setSize(sizeDim);
    setPreferredSize(sizeDim);
    setMaximumSize(sizeDim);
    JLabel lblTeamName = new JLabel("Team name:");

    teamNameTF = new JTextField();
    teamNameTF.setColumns(10);

    nationalTeamCB = new JCheckBox("National team");
    nationalTeamCB.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
          teamNameTF.setText(null);
          teamNameTF.setEditable(false);
        } else {
          teamNameTF.setEditable(true);
        }
      }
    });

    JLabel lblCountry = new JLabel("Country:");
    countryCB = new JComboBox(countries);

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout
        .createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup().addContainerGap()
            .addGroup(groupLayout
                .createParallelGroup(Alignment.LEADING, false)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(lblTeamName).addGap(18).addComponent(
                        teamNameTF, GroupLayout.PREFERRED_SIZE,
                        134, GroupLayout.PREFERRED_SIZE))
                .addGroup(
                    groupLayout.createSequentialGroup().addComponent(lblCountry)
                        .addPreferredGap(ComponentPlacement.RELATED,
                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(countryCB, GroupLayout.PREFERRED_SIZE,
                            134, GroupLayout.PREFERRED_SIZE))
                .addComponent(nationalTeamCB))
            .addContainerGap(29, Short.MAX_VALUE)));
    groupLayout
        .setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup().addGap(11)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblTeamName).addComponent(teamNameTF,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(nationalTeamCB)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblCountry).addComponent(countryCB,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE)));
    setLayout(groupLayout);
  }

  public boolean isInputValid() {
    boolean countryValid = countryCB.getSelectedItem() == null ? false : true;
    if (nationalTeamCB.isSelected()) {
      return countryValid;
    }

    boolean teamNameValid = StringUtils.isEmpty(teamNameTF.getText()) ? false
        : true;

    return countryValid && teamNameValid;
  }

  public Team getTeam() {
    boolean nationalTeam = nationalTeamCB.isSelected();
    String country = (String) countryCB.getSelectedItem();
    String teamName = nationalTeam ? country : teamNameTF.getText();

    return new Team(null, teamName, country, nationalTeam);
  }
}
