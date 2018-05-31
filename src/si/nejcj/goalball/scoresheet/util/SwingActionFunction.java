package si.nejcj.goalball.scoresheet.util;

import java.awt.event.ActionEvent;

@FunctionalInterface
public interface SwingActionFunction {
  
  public void performAction(ActionEvent e);
}
