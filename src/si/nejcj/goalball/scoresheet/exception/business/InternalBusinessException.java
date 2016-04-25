package si.nejcj.goalball.scoresheet.exception.business;

import si.nejcj.goalball.scoresheet.exception.InternalException;

public class InternalBusinessException extends InternalException {
  private static final long serialVersionUID = 2648162408981302944L;

  public InternalBusinessException(Throwable cause) {
    super(cause);
  }

  public InternalBusinessException(String message) {
    super(message);
  }

  public InternalBusinessException(String message, Throwable cause) {
    super(message, cause);
  }
}
