package si.nejcj.goalball.scoresheet.tournament.panel;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import si.nejcj.goalball.scoresheet.util.gui.PlayerNumberTableRenderer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ManageTournamentTeamPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private JTable staffTBL;
  private JTable playersTBL;

  public ManageTournamentTeamPanel(String teamName,
      TableModel playersTableModel, TableModel staffTableModel,
      Object[] playerNumbers) {

    JLabel teamNameLBL = new JLabel(teamName);
    teamNameLBL.setFont(new Font("Tahoma", Font.BOLD, 14));

    JScrollPane staffSP = new JScrollPane();
    JScrollPane playersSP = new JScrollPane();

    JLabel staffLBL = new JLabel("Staff members");
    JLabel playersLBL = new JLabel("Players");

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout
        .setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        groupLayout
                            .createParallelGroup(Alignment.LEADING)
                            .addComponent(teamNameLBL)
                            .addComponent(staffLBL)
                            .addComponent(playersLBL)
                            .addComponent(playersSP, GroupLayout.DEFAULT_SIZE,
                                334, Short.MAX_VALUE)
                            .addComponent(staffSP, 0, 0, Short.MAX_VALUE))
                    .addGap(106)));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addComponent(teamNameLBL)
            .addGap(13)
            .addComponent(staffLBL)
            .addPreferredGap(ComponentPlacement.UNRELATED)
            .addComponent(staffSP, GroupLayout.PREFERRED_SIZE, 198,
                GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(playersLBL)
            .addPreferredGap(ComponentPlacement.UNRELATED)
            .addComponent(playersSP, GroupLayout.PREFERRED_SIZE, 198,
                GroupLayout.PREFERRED_SIZE).addGap(76)));

    staffTBL = new JTable();
    staffTBL.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    staffTBL.getTableHeader().setReorderingAllowed(false);
    staffTBL.setSurrendersFocusOnKeystroke(true);
    staffTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    staffSP.setViewportView(staffTBL);
    staffTBL.doLayout();
    staffTBL.setModel(staffTableModel);

    playersTBL = new JTable();
    playersTBL.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    playersTBL.getTableHeader().setReorderingAllowed(false);
    playersTBL.setSurrendersFocusOnKeystroke(true);
    playersTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playersTBL.setDefaultRenderer(Integer.class,
        new PlayerNumberTableRenderer());

    // JComboBox playerNumbersCB = new JComboBox(playerNumbers);
    JComboBox playerNumbersCB = new JComboBox();
    playerNumbersCB.setModel(new DefaultComboBoxModel(playerNumbers));
    playerNumbersCB.setEditable(false);
    playerNumbersCB.setRenderer(new PlayerNumberListRenderer());
    playersTBL.setDefaultEditor(Integer.class, new DefaultCellEditor(
        playerNumbersCB));
    playersSP.setViewportView(playersTBL);
    playersTBL.doLayout();
    playersTBL.setModel(playersTableModel);

    setLayout(groupLayout);
  }

  class PlayerNumberListRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = -2653934006307979304L;

    public PlayerNumberListRenderer() {
      setOpaque(true);
      setHorizontalAlignment(LEFT);
      setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      if (isSelected) {
        setBackground(list.getSelectionBackground());
        setForeground(list.getSelectionForeground());
      } else {
        setBackground(list.getBackground());
        setForeground(list.getForeground());
      }

      if (value == null) {
        setText("");
        return this;
      } else if (value instanceof Integer) {
        Integer playerNumber = (Integer) value;

        setText(playerNumber.toString());
        return this;
      }
      return null;
    }
  }
}