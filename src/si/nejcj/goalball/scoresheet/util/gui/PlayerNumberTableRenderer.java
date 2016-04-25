package si.nejcj.goalball.scoresheet.util.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class PlayerNumberTableRenderer extends JLabel implements
    TableCellRenderer {
  private static final long serialVersionUID = -2653934006307979304L;

  public PlayerNumberTableRenderer() {
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

    if (value instanceof Integer) {
      Integer playerNumber = (Integer) value;
      setText(playerNumber.toString());
      return this;
    }

    return null;
  }
}