package si.nejcj.goalball.scoresheet.exception;

/**
 * Represents an internal exception in the application.
 * 
 * @author Nejc Jakic
 * 
 */
public class InternalException extends RuntimeException {
  private static final long serialVersionUID = 2824536167251567872L;

  public InternalException(Throwable cause) {
    super(cause);
  }

  public InternalException(String message) {
    super(message);
  }

  public InternalException(String message, Throwable cause) {
    super(message, cause);
  }
}
