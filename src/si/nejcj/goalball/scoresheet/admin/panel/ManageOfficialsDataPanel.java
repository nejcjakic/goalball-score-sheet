package si.nejcj.goalball.scoresheet.admin.panel;

import java.awt.Component;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import si.nejcj.goalball.scoresheet.admin.model.OfficialsTableModel;
import si.nejcj.goalball.scoresheet.db.DatabaseUtil;
import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;
import si.nejcj.goalball.scoresheet.util.gui.OfficialsLevelTableRenderer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ManageOfficialsDataPanel extends JPanel {

  private static final long serialVersionUID = 1l;

  private JTable officialsTBL;

  public ManageOfficialsDataPanel(TableModel officialsTableModel,
      ComboBoxModel officialLevelsModel,
      TableModelListener officialsTableModelListener,
      AbstractAction addOfficialButtonAction,
      AbstractAction deleteOfficialButtonAction) {
    JScrollPane officialsSP = new JScrollPane();
    JLabel officialsLBL = new JLabel("Officials");
    JButton addOfficialBtn = new JButton(addOfficialButtonAction);
    JButton deleteOfficialBtn = new JButton(deleteOfficialButtonAction);

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addContainerGap()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addComponent(officialsLBL)
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addComponent(officialsSP,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(
                                groupLayout
                                    .createParallelGroup(Alignment.LEADING)
                                    .addComponent(deleteOfficialBtn)
                                    .addComponent(addOfficialBtn))))
            .addContainerGap(60, Short.MAX_VALUE)));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
        Alignment.LEADING).addGroup(
        groupLayout
            .createSequentialGroup()
            .addGroup(
                groupLayout
                    .createParallelGroup(Alignment.LEADING)
                    .addGroup(
                        groupLayout
                            .createSequentialGroup()
                            .addContainerGap()
                            .addComponent(officialsLBL)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(officialsSP,
                                GroupLayout.PREFERRED_SIZE, 198,
                                GroupLayout.PREFERRED_SIZE))
                    .addGroup(
                        groupLayout.createSequentialGroup().addGap(56)
                            .addComponent(addOfficialBtn)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(deleteOfficialBtn)))
            .addContainerGap(66, Short.MAX_VALUE)));

    officialsTBL = new JTable(officialsTableModel);
    officialsTBL.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    officialsTBL.getTableHeader().setReorderingAllowed(false);
    officialsTBL.setSurrendersFocusOnKeystroke(true);
    officialsTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    officialsTBL.getModel().addTableModelListener(officialsTableModelListener);
    officialsTBL.setDefaultRenderer(OfficialLevel.class,
        new OfficialsLevelTableRenderer());
    JComboBox officialLevelsComboBox = new JComboBox(officialLevelsModel);
    officialLevelsComboBox.setEditable(false);
    officialLevelsComboBox.setRenderer(new OfficialsLevelListRenderer());
    officialsTBL.setDefaultEditor(OfficialLevel.class, new DefaultCellEditor(
        officialLevelsComboBox));
    Object[] genders = { "male", "female" };
    JComboBox genderComboBox = new JComboBox(genders);
    TableColumn genderColumn = officialsTBL.getColumnModel().getColumn(
        OfficialsTableModel.COLUMN_IDX_GENDER);
    genderColumn.setCellEditor(new DefaultCellEditor(genderComboBox));
    JComboBox countryComboBox = new JComboBox();
    List<String> countries = DatabaseUtil.getListOfCountries();
    countryComboBox.addItem(null);
    for (String country : countries) {
      countryComboBox.addItem(country);
    }
    TableColumn countryColumn = officialsTBL.getColumnModel().getColumn(
        OfficialsTableModel.COLUMN_IDX_COUNTRY);
    countryColumn.setCellEditor(new DefaultCellEditor(countryComboBox));
    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
        officialsTableModel);
    officialsTBL.setRowSorter(sorter);
    officialsSP.setViewportView(officialsTBL);

    // Set column widths
    JLabel dimLbl = new JLabel("Very Long Lastname");
    int dim = dimLbl.getPreferredSize().width;
    officialsTBL.getColumnModel()
        .getColumn(OfficialsTableModel.COLUMN_IDX_LAST_NAME)
        .setPreferredWidth(dim);
    dimLbl = new JLabel("Long Firstname");
    dim = dimLbl.getPreferredSize().width;
    officialsTBL.getColumnModel()
        .getColumn(OfficialsTableModel.COLUMN_IDX_FIRST_NAME)
        .setPreferredWidth(dim);
    dimLbl = new JLabel("Gen female");
    dim = dimLbl.getPreferredSize().width;
    officialsTBL.getColumnModel()
        .getColumn(OfficialsTableModel.COLUMN_IDX_GENDER)
        .setPreferredWidth(dim);
    dimLbl = new JLabel("Long Country Name");
    dim = dimLbl.getPreferredSize().width;
    officialsTBL.getColumnModel()
        .getColumn(OfficialsTableModel.COLUMN_IDX_COUNTRY)
        .setPreferredWidth(dim);
    dimLbl = new JLabel("Very high level");
    dim = dimLbl.getPreferredSize().width;
    officialsTBL.getColumnModel()
        .getColumn(OfficialsTableModel.COLUMN_IDX_OFFICIAL_LEVEL)
        .setPreferredWidth(dim);

    officialsTBL.doLayout();
    setLayout(groupLayout);
  }

  public int getSelectedRow() {
    if (officialsTBL.getSelectedRow() == -1) {
      return -1;
    }
    return officialsTBL.convertRowIndexToModel(officialsTBL.getSelectedRow());
  }

  class OfficialsLevelListRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = -2653934006307979304L;

    public OfficialsLevelListRenderer() {
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

      if (value instanceof OfficialLevel) {
        OfficialLevel level = (OfficialLevel) value;
        setText(level.getLevelName());
        return this;
      }
      return null;
    }
  }
}
