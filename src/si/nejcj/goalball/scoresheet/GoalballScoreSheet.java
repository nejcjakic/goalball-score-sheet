package si.nejcj.goalball.scoresheet;

/**
 * Entry class of the GoalballScoreSheet application. Starts the application in
 * the event-dispatching thread.
 * 
 * @author nejcj
 * 
 */
public class GoalballScoreSheet {
  public static void main(String[] args) {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new MainController();
      }
    });
  }
}
