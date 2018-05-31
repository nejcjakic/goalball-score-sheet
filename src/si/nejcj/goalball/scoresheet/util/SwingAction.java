package si.nejcj.goalball.scoresheet.util;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class SwingAction {

//  public static AbstractAction of( final Consumer consumer) {
//    return new AbstractAction() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        consumer.accept(e);
//      }
//    };
//  }

  public static AbstractAction of(final String name,
      final SwingActionFunction swingAction) {
    return new AbstractAction(name) {

      @Override
      public void actionPerformed(ActionEvent e) {
        swingAction.performAction(e);
      }
    };
  }

  public static AbstractAction of(final SwingActionFunction swingAction) {
    return new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        swingAction.performAction(e);
      }
    };
  }
}
