package si.nejcj.goalball.scoresheet.game.panel;

import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.CaretListener;

import si.nejcj.goalball.scoresheet.util.PdfFieldConstants;

@SuppressWarnings("serial")
@Deprecated
public class GeneralGameInformationPanel extends JPanel {
  private JTextField gameNumberTF;
  private JTextField dateOfGameTF;
  private JTextField timeOfGameTF;
  private JTextField divisionPoolTF;
  private JTextField venueTF;
  private final ButtonGroup genderButtonGroup = new ButtonGroup();

  public GeneralGameInformationPanel(Map<String, EventListener> changeListeners) {

    JLabel dateOfGameLBL = new JLabel("Date of game");
    JLabel timeOfGameLBL = new JLabel("Time of game");
    JLabel gameNumberLBL = new JLabel("Game number");
    JLabel divisionPoolLBL = new JLabel("Division/Pool");
    JLabel genderLBL = new JLabel("Gender");
    JLabel venueLBL = new JLabel("Venue");

    gameNumberTF = new JTextField();
    gameNumberLBL.setLabelFor(gameNumberTF);
    gameNumberTF.addCaretListener((CaretListener) changeListeners
        .get(PdfFieldConstants.FIELD_GAME_NUMBER));
    gameNumberTF.setColumns(10);

    dateOfGameTF = new JTextField();
    dateOfGameLBL.setLabelFor(dateOfGameTF);
    dateOfGameTF.addCaretListener((CaretListener) changeListeners
        .get(PdfFieldConstants.FIELD_DATE_OF_GAME));
    dateOfGameTF.setColumns(10);

    timeOfGameTF = new JTextField();
    timeOfGameLBL.setLabelFor(timeOfGameTF);
    timeOfGameTF.addCaretListener((CaretListener) changeListeners
        .get(PdfFieldConstants.FIELD_TIME_OF_GAME));
    timeOfGameTF.setColumns(10);

    divisionPoolTF = new JTextField();
    divisionPoolLBL.setLabelFor(divisionPoolTF);
    divisionPoolTF.addCaretListener((CaretListener) changeListeners
        .get(PdfFieldConstants.FIELD_DIVISION_POOL));
    divisionPoolTF.setColumns(10);

    venueTF = new JTextField();
    venueLBL.setLabelFor(venueTF);
    venueTF.addCaretListener((CaretListener) changeListeners
        .get(PdfFieldConstants.FIELD_VENUE));
    venueTF.setColumns(30);

    JRadioButton genderMaleRB = new JRadioButton("Male");
    genderMaleRB.addActionListener((ActionListener) changeListeners
        .get(PdfFieldConstants.FIELD_GENDER));
    genderButtonGroup.add(genderMaleRB);

    JRadioButton genderFemaleRB = new JRadioButton("Female");
    genderFemaleRB.addActionListener((ActionListener) changeListeners
        .get(PdfFieldConstants.FIELD_GENDER));
    genderButtonGroup.add(genderFemaleRB);
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
                            .createSequentialGroup()
                            .addComponent(gameNumberLBL)
                            .addGap(18)
                            .addComponent(gameNumberTF,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addGroup(
                                groupLayout
                                    .createParallelGroup(Alignment.LEADING)
                                    .addComponent(dateOfGameLBL)
                                    .addComponent(timeOfGameLBL)
                                    .addComponent(divisionPoolLBL)
                                    .addComponent(genderLBL)
                                    .addComponent(venueLBL))
                            .addGap(18)
                            .addGroup(
                                groupLayout
                                    .createParallelGroup(Alignment.LEADING)
                                    .addGroup(
                                        groupLayout
                                            .createSequentialGroup()
                                            .addComponent(genderMaleRB)
                                            .addPreferredGap(
                                                ComponentPlacement.RELATED)
                                            .addComponent(genderFemaleRB))
                                    .addComponent(venueTF,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addComponent(divisionPoolTF,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateOfGameTF,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timeOfGameTF,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))))
            .addContainerGap(201, Short.MAX_VALUE)));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.BASELINE)
                    .addComponent(gameNumberLBL)
                    .addComponent(gameNumberTF, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.BASELINE)
                    .addComponent(dateOfGameLBL)
                    .addComponent(dateOfGameTF, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.BASELINE)
                    .addComponent(timeOfGameLBL)
                    .addComponent(timeOfGameTF, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.BASELINE)
                    .addComponent(divisionPoolLBL)
                    .addComponent(divisionPoolTF, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(
                groupLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(genderLBL).addComponent(genderMaleRB)
                    .addComponent(genderFemaleRB))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.BASELINE)
                    .addComponent(venueLBL)
                    .addComponent(venueTF, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addContainerGap(139, Short.MAX_VALUE)));
    setLayout(groupLayout);

  }
}
