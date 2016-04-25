package si.nejcj.goalball.scoresheet.exception.technical;

import si.nejcj.goalball.scoresheet.exception.InternalException;

public class InternalTechnicalException extends InternalException {
  private static final long serialVersionUID = -4490932374873753126L;

  public InternalTechnicalException(Throwable cause) {
    super(cause);
  }

  public InternalTechnicalException(String message) {
    super(message);
  }

  public InternalTechnicalException(String message, Throwable cause) {
    super(message, cause);
  }
}
