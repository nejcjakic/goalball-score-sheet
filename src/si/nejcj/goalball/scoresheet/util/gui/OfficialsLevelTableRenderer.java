package si.nejcj.goalball.scoresheet.util.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;

public class OfficialsLevelTableRenderer extends JLabel implements
    TableCellRenderer {
  private static final long serialVersionUID = -2653934006307979304L;

  public OfficialsLevelTableRenderer() {
    setOpaque(true);
    setHorizontalAlignment(LEFT);
    setVerticalAlignment(CENTER);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setBackground(table.getSelectionBackground());
      setForeground(table.getSelectionForeground());
    } else {
      setBackground(table.getBackground());
      setForeground(table.getForeground());
    }

    if (value instanceof OfficialLevel) {
      OfficialLevel level = (OfficialLevel) value;
      setText(level.getLevelName());
      return this;
    }

    return null;
  }
}